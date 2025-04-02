package com.example.focuslink.core.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Timer : Screen("timer")
    object Stats : Screen("stats")
    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
}