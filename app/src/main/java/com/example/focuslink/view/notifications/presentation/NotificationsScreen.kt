package com.example.focuslink.view.notifications.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.focuslink.ui.theme.PinkPrimary

@Composable
fun NotificationsScreen(
    notificationsViewModel: NotificationsViewModel,
    navigateToTimer: () -> Unit,
    navigateToStats: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val uiState by notificationsViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Notificaciones",
            style = MaterialTheme.typography.headlineMedium,
            color = PinkPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${uiState.notifications.size} notificaciones",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PinkPrimary)
            }
        } else if (uiState.notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "No tienes notificaciones",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(uiState.notifications) { notification ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (notification.isRead)
                                        MaterialTheme.colorScheme.surfaceVariant
                                    else
                                        PinkPrimary.copy(alpha = 0.1f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = if (notification.isRead)
                                    MaterialTheme.colorScheme.outline
                                else
                                    PinkPrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = notification.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = notification.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = notification.timestamp,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        if (!notification.isRead) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(PinkPrimary)
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier.padding(start = 64.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }

        NavigationBar {
            NavigationBarItem(
                selected = false,
                onClick = navigateToTimer,
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
                selected = true,
                onClick = { },
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
