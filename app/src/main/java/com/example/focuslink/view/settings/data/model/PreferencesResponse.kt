package com.example.focuslink.view.settings.data.model

data class PreferencesResponse (
    val id: String,
    val userId: String,
    val focusTime: Int,
    val shortBreak:Int,
    val longBreak: Int,
    val vibration: Boolean,
    val sound: Boolean,
    val theme: String
)