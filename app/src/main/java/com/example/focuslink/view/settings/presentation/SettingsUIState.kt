package com.example.focuslink.view.settings.presentation

data class SettingsUIState(
    val username: String = "Usuario",
    val email: String = "usuario@example.com",
    val focusTimeMinutes: Int = 25,
    val breakTimeMinutes: Int = 5,
    val notificationsEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrateEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)