package com.example.focuslink.view.stats.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.focuslink.components.BottomNavigationBar
import com.example.focuslink.core.navigation.Screen
import com.example.focuslink.ui.theme.PinkPrimary

@Composable
fun StatsScreen(
    statsViewModel: StatsViewModel,
    navigateToTimer: () -> Unit,
    navigateToNotifications: () -> Unit,
    navigateToSettings: () -> Unit,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val uiState by statsViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Estadísticas",
            style = MaterialTheme.typography.headlineMedium,
            color = PinkPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PinkPrimary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Primera fila: Ciclos y Tiempo total
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Ciclos totales
                    StatCard(
                        icon = Icons.Default.BarChart,
                        value = uiState.totalCycles.toString(),
                        label = "Ciclos",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )

                    // Tiempo total
                    StatCard(
                        icon = Icons.Default.AccessTime,
                        value = "${uiState.totalHours}h ${uiState.totalMinutes}m",
                        label = "Tiempo total",
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Segunda fila: Descansos cortos y Descansos largos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Descansos cortos
                    StatCard(
                        icon = Icons.Default.Coffee,
                        value = uiState.breakCount.toString(),
                        label = "Descansos cortos",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )

                    // Descansos largos
                    StatCard(
                        icon = Icons.Default.HotelClass,
                        value = uiState.longBreakCount.toString(),
                        label = "Descansos largos",
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interrupciones
                StatCard(
                    icon = Icons.Default.NotificationsActive,
                    value = uiState.totalInterruptions.toString(),
                    label = "Interrupciones",
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            }
        }

        BottomNavigationBar(
            currentRoute = Screen.Stats.route,
            onNavigateToTimer = navigateToTimer,
            onNavigateToStats = { /* Ya estás aquí */ },
            onNavigateToNotifications = navigateToNotifications,
            onNavigateToSettings = navigateToSettings,
            modifier = Modifier.fillMaxWidth(),
            isDarkTheme = isDarkTheme
        )
    }
}

@Composable
fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primaryContainer
) {
    Card(
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = PinkPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}