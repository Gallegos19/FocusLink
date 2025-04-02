package com.example.focuslink.view.settings.data.model

data class PreferencesRequest(
    val userId: String,
    val focusTimeMinutes: Int,
    val breakTimeMinutes: Int,
    val notificationsEnabled: Boolean,
    val soundEnabled: Boolean,
    val vibrateEnabled: Boolean,
    val darkModeEnabled: Boolean
)