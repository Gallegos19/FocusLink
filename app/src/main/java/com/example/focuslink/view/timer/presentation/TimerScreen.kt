package com.example.focuslink.view.timer.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.focuslink.components.BottomNavigationBar
import com.example.focuslink.core.navigation.Screen
import com.example.focuslink.ui.theme.PinkPrimary
import com.example.focuslink.utils.startFocusNotification
import com.example.focuslink.view.timer.presentation.components.TimerCircle
import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay

@Composable
fun TimerScreen(
    timerViewModel: TimerViewModel,
    navigateToStats: () -> Unit,
    navigateToNotifications: () -> Unit,
    navigateToSettings: () -> Unit,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val uiState by timerViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Variables para control de UI
    var isAlarmButtonVisible by remember { mutableStateOf(false) }
    var timerCompletedCount by remember { mutableStateOf(0) }

    // Para garantizar que tengamos acceso al contexto incluso en callbacks
    val appContext = context.applicationContext

    // Lanzador de permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Verificar permisos concedidos
        permissions.entries.forEach { entry ->
            Log.d("PermisoDebug", "Permiso ${entry.key}: ${entry.value}")
        }
    }

    // Solicitar permisos al iniciar
    LaunchedEffect(Unit) {
        val permissionsToRequest = mutableListOf<String>()
        permissionsToRequest.add(Manifest.permission.VIBRATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        permissionLauncher.launch(permissionsToRequest.toTypedArray())
    }

    // Monitorear eventos del temporizador
    LaunchedEffect(uiState.isRunning, uiState.timeLeftFormatted, uiState.progress) {
        // Para la notificación
        if (uiState.isRunning) {
            context.startFocusNotification(
                timeLeft = uiState.timeLeftFormatted,
                progress = uiState.progress
            )
        }

        // Debug: imprimir todos los valores para verificar la condición
        if (!uiState.isRunning) {
            Log.d("AlarmDebug", "Timer no está corriendo")
        }
        if (uiState.progress <= 0.05f) {
            Log.d("AlarmDebug", "Progreso menor a 0.05: ${uiState.progress}")
        }
        if (uiState.timeLeftFormatted == "00:00") {
            Log.d("AlarmDebug", "Tiempo es 00:00")
        }

        // Verificar condición exactamente como está escrita
        val conditionMet = !uiState.isRunning && uiState.progress <= 0.05f && uiState.timeLeftFormatted == "00:00"

        // Verificar si el temporizador ha terminado
        if (conditionMet) {
            timerCompletedCount += 1

            // Reproducir alarma con el contexto actual
            timerViewModel.playAlarmSound(appContext)

            // Mostrar el botón de silenciar alarma
            isAlarmButtonVisible = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FocusLink",
            style = MaterialTheme.typography.headlineMedium,
            color = PinkPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de información del ciclo
        Text(
            text = "Ciclo #${uiState.currentCycle}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (uiState.isBreakTime) "Tiempo de descanso" else "Tiempo de enfoque",
            style = MaterialTheme.typography.bodyLarge,
            color = if (uiState.isBreakTime) Color.Green else PinkPrimary
        )

        // Indicador de finalización (para depuración)
        if (timerCompletedCount > 0) {
            Text(
                text = "Finalizaciones: $timerCompletedCount",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Timer Circle
        TimerCircle(
            progress = uiState.progress,
            timeLeft = uiState.timeLeftFormatted,
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de control principal
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Botón de play/pause
            FloatingActionButton(
                onClick = {
                    if (uiState.isRunning) {
                        timerViewModel.pauseTimer()
                    } else {
                        timerViewModel.startTimer()
                    }
                },
                containerColor = if (uiState.isRunning) MaterialTheme.colorScheme.secondary else PinkPrimary
            ) {
                Icon(
                    imageVector = if (uiState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (uiState.isRunning) "Pausar" else "Iniciar"
                )
            }

            // Botón de stop
            if (uiState.isRunning || uiState.isPaused) {
                FloatingActionButton(
                    onClick = { timerViewModel.stopTimer() },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = "Detener"
                    )
                }
            }

            // Botón para silenciar alarma que aparece cuando suena la alarma
            if (isAlarmButtonVisible) {
                FloatingActionButton(
                    onClick = {
                        timerViewModel.stopAlarmSound(appContext)
                        isAlarmButtonVisible = false
                    },
                    containerColor = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(64.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeOff,
                            contentDescription = "Silenciar alarma"
                        )
                        Text(
                            text = "Silenciar",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        /*
        // Botones de prueba (solo para desarrollo)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    Log.d("AlarmDebug", "Botón de prueba presionado")
                    timerViewModel.playAlarmSound(appContext)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Probar alarma"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("PROBAR ALARMA")
            }

            Button(
                onClick = {
                    timerViewModel.stopAlarmSound(appContext)
                    isAlarmButtonVisible = false
                }
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeOff,
                    contentDescription = "Detener alarma"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("DETENER ALARMA")
            }
        }*/

        Spacer(modifier = Modifier.weight(1f))

        // Barra de navegación
        BottomNavigationBar(
            currentRoute = Screen.Timer.route,
            onNavigateToTimer = { /* Ya estás aquí */ },
            onNavigateToStats = navigateToStats,
            onNavigateToNotifications = navigateToNotifications,
            onNavigateToSettings = navigateToSettings,
            modifier = Modifier.fillMaxWidth(),
            isDarkTheme = isDarkTheme
        )
    }
}