package com.example.focuslink.view.settings.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.focuslink.ui.theme.PinkPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    navigateToTimer: () -> Unit,
    navigateToStats: () -> Unit,
    navigateToNotifications: () -> Unit
) {
    val uiState by settingsViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                        listOf(15, 25, 30, 45).forEach { minutes ->
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
                        listOf(5, 10, 15, 20).forEach { minutes ->
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
                        onCheckedChange = settingsViewModel::toggleNotifications
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    SwitchRow(
                        label = "Sonido",
                        checked = uiState.soundEnabled,
                        enabled = uiState.notificationsEnabled,
                        onCheckedChange = settingsViewModel::toggleSound
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    SwitchRow(
                        label = "Vibración",
                        checked = uiState.vibrateEnabled,
                        enabled = uiState.notificationsEnabled,
                        onCheckedChange = settingsViewModel::toggleVibrate
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
        NavigationBar {
            NavigationBarItem(
                selected = false,
                onClick = navigateToTimer,
                icon = { Icon(Icons.Default.Timer, contentDescription = "Timer") },
                label = { Text("Temporizador") }
            )
            NavigationBarItem(
                selected = false,
                onClick = navigateToStats,
                icon = { Icon(Icons.Default.BarChart, contentDescription = "Estadísticas") },
                label = { Text("Estadísticas") }
            )
            NavigationBarItem(
                selected = false,
                onClick = navigateToNotifications,
                icon = { Icon(Icons.Default.Notifications, contentDescription = "Notificaciones") },
                label = { Text("Notificaciones") }
            )
            NavigationBarItem(
                selected = true,
                onClick = { /* Ya estás aquí */ },
                icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") },
                label = { Text("Configuración") }
            )
        }
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
