package com.example.focuslink.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.focuslink.core.navigation.Screen
import com.example.focuslink.ui.theme.PinkPrimary

// Color morado para la barra de navegación en modo oscuro
val NavBarPurple = Color(0xFF6C63FF)

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigateToTimer: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    // Usamos colores diferentes dependiendo del tema
    val backgroundColor = if (isDarkTheme) NavBarPurple else MaterialTheme.colorScheme.surface
    val contentColor = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.onSurface
    val unselectedContentColor = if (isDarkTheme)
        Color.White.copy(alpha = 0.7f)
    else
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

    // El color del indicador es PinkPrimary en ambos temas
    val indicatorColor = PinkPrimary

    NavigationBar(
        modifier = modifier,
        containerColor = backgroundColor,
        contentColor = contentColor
    ) {
        NavigationBarItem(
            selected = currentRoute == Screen.Timer.route,
            onClick = onNavigateToTimer,
            icon = { Icon(Icons.Default.Timer, contentDescription = "Temporizador") },
            label = { Text("") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = contentColor,
                unselectedIconColor = unselectedContentColor,
                selectedTextColor = contentColor,
                unselectedTextColor = unselectedContentColor,
                indicatorColor = indicatorColor
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Stats.route,
            onClick = onNavigateToStats,
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Estadísticas") },
            label = { Text("") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = contentColor,
                unselectedIconColor = unselectedContentColor,
                selectedTextColor = contentColor,
                unselectedTextColor = unselectedContentColor,
                indicatorColor = indicatorColor
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Notifications.route,
            onClick = onNavigateToNotifications,
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notificaciones") },
            label = { Text("") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = contentColor,
                unselectedIconColor = unselectedContentColor,
                selectedTextColor = contentColor,
                unselectedTextColor = unselectedContentColor,
                indicatorColor = indicatorColor
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Settings.route,
            onClick = onNavigateToSettings,
            icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") },
            label = { Text("") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = contentColor,
                unselectedIconColor = unselectedContentColor,
                selectedTextColor = contentColor,
                unselectedTextColor = unselectedContentColor,
                indicatorColor = indicatorColor
            )
        )
    }
}