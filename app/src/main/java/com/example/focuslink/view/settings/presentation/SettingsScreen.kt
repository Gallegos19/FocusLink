package com.example.focuslink.view.settings.presentation

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.focuslink.components.BottomNavigationBar
import com.example.focuslink.core.navigation.Screen
import com.example.focuslink.core.services.FocusLinkNotificationListener
import com.example.focuslink.ui.theme.PinkPrimary
import com.example.focuslink.utils.PermissionRequester

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    navigateToTimer: () -> Unit,
    navigateToStats: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToNotifications: () -> Unit,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val uiState by settingsViewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(uiState) {
        println("Nuevo estado de configuración: $uiState")
    }
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }


    LaunchedEffect(uiState.logout) {
        if (uiState.logout){
            navigateToLogin()
        }
    }

    val notiPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show()
            settingsViewModel.solicitarTokenFCM()
        } else {
            Toast.makeText(context, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
        }
    }
    val vibratePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permiso de vibración concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permiso de vibración denegado", Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        NotificationPermissionButton()
        // Título
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.headlineMedium,
            color = PinkPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Sección Perfil
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Perfil", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = PinkPrimary, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(uiState.username, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                            Text(uiState.email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // Sección Temporizador
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Temporizador", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Tiempo de enfoque", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        listOf(1, 25, 30, 45).forEach { minutes ->
                            FilterChip(
                                selected = uiState.focusTimeMinutes == minutes,
                                onClick = { settingsViewModel.updateFocusTime(minutes) },
                                label = { Text("$minutes min") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PinkPrimary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Tiempo de descanso", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        listOf(1, 10, 15, 20).forEach { minutes ->
                            FilterChip(
                                selected = uiState.breakTimeMinutes == minutes,
                                onClick = { settingsViewModel.updateBreakTime(minutes) },
                                label = { Text("$minutes min") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PinkPrimary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }
            }

            // Sección Notificaciones
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Notificaciones", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(16.dp))

                    SwitchRow(
                        label = "Activar notificaciones",
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = {
                            settingsViewModel.toggleNotifications(it)

                            if (it) {
                                // Solo pedimos permiso si las activan
                                PermissionRequester.pedirPermisos(context, Manifest.permission.POST_NOTIFICATIONS, notiPermissionLauncher)
                            }
                        }
                    )
                }
            }

            // Sección Tema
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tema", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(16.dp))
                    SwitchRow(
                        label = "Modo oscuro",
                        checked = uiState.darkModeEnabled,
                        onCheckedChange = settingsViewModel::toggleDarkMode
                    )
                }
            }

            // Cerrar sesión
            Button(
                onClick = settingsViewModel::logout,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar sesión")
            }

            Text(
                text = "FocusLink v1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(vertical = 16.dp).align(Alignment.CenterHorizontally)
            )
        }

        // Navegación inferior
        BottomNavigationBar(
            currentRoute = Screen.Settings.route,
            onNavigateToTimer = navigateToTimer,
            onNavigateToStats = navigateToStats,
            onNavigateToNotifications = navigateToNotifications,
            onNavigateToSettings = { /* Ya estás aquí */ },
            modifier = Modifier.fillMaxWidth(),
            isDarkTheme = isDarkTheme
        )
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = PinkPrimary
            )
        )
    }
}

@Composable
fun NotificationPermissionButton() {
    val context = LocalContext.current

    if (!isNotificationServiceEnabled(context)) {
        Button(onClick = {
            // Abre los ajustes de acceso a notificaciones
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            context.startActivity(intent)

            // Después de enviar al usuario, reinicia el servicio
            restartNotificationListenerService(context)
        }) {
            Text("Activar acceso a notificaciones")
        }
    } else {
        Log.d("NotificationAccess", "El permiso ya está concedido")
    }
}


// Función para verificar si el permiso está activo
fun isNotificationServiceEnabled(context: Context): Boolean {
    val pkgName = context.packageName
    val flat = Settings.Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    )
    return flat?.contains(pkgName) == true
}
fun restartNotificationListenerService(context: Context) {
    val componentName = ComponentName(context, FocusLinkNotificationListener::class.java)
    val pm = context.packageManager
    pm.setComponentEnabledSetting(
        componentName,
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
        PackageManager.DONT_KILL_APP
    )
    pm.setComponentEnabledSetting(
        componentName,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )
}
