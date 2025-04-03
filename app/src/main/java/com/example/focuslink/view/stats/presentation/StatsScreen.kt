package com.example.focuslink.view.stats.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
                    .padding(end = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.BarChart, contentDescription = null, tint = PinkPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.totalCycles.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Ciclos", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
                    .padding(start = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = PinkPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${uiState.totalHours}h ${uiState.totalMinutes}m",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Tiempo total", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Estadísticas diarias",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
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
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(uiState.dailyStats.size) { index ->
                    val dailyStat = uiState.dailyStats[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = PinkPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = dailyStat.date,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${dailyStat.focusTime} • ${dailyStat.cycles} ciclos",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (index < uiState.dailyStats.size - 1) {
                        Divider(
                            modifier = Modifier.padding(start = 48.dp, top = 8.dp, bottom = 8.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
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
