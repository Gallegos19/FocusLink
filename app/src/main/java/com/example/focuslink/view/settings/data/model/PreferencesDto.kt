package com.example.focuslink.view.settings.data.model

data class PreferencesDto(
    val userId: String,
    val username: String,
    val email: String,
    val focusTimeMinutes: Int,
    val breakTimeMinutes: Int,
    val notificationsEnabled: Boolean,
    val soundEnabled: Boolean,
    val vibrateEnabled: Boolean,
    val darkModeEnabled: Boolean
)