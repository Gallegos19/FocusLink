package com.example.focuslink.view.settings.data.model

data class PreferencesRequest(
    val focusTime: Int,
    val shortBreak: Int,
    val longBreak: Int,
    val vibration: Boolean,
    val sound: Boolean,
    val theme: String
)