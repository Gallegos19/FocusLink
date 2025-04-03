package com.example.focuslink.core.data.local.preferences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferences")
data class PreferencesEntity(
    @PrimaryKey val userId: String,
    val focusTime: Int,
    val shortBreak: Int,
    val longBreak: Int,
    val vibration: Boolean,
    val sound: Boolean,
    val theme: String // "light" o "dark"
)
