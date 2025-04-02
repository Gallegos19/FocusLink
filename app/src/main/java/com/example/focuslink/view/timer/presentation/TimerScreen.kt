package com.example.focuslink.view.timer.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.focuslink.ui.theme.PinkPrimary
import com.example.focuslink.view.timer.presentation.components.TimerCircle

@Composable
fun TimerScreen(
    timerViewModel: TimerViewModel,
    navigateToStats: () -> Unit,
    navigateToNotifications: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val uiState by timerViewModel.uiState.collectAsState()

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

        Spacer(modifier = Modifier.height(32.dp))

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

        Spacer(modifier = Modifier.height(32.dp))

        TimerCircle(
            progress = uiState.progress,
            timeLeft = uiState.timeLeftFormatted,
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
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
        }

        Spacer(modifier = Modifier.weight(1f))

        NavigationBar {
            NavigationBarItem(
                selected = true,
                onClick = { /* Ya estás aquí */ },
                icon = { Icon(Icons.Default.Timer, contentDescription = "Temporizador") },
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
                selected = false,
                onClick = navigateToSettings,
                icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") },
                label = { Text("Configuración") }
            )
        }
    }
}
