package com.example.focuslink.view.timer.data.model

import java.util.Date

data class SessionDto(
    val id: String,
    val userId: String,
    val startTime: Date,
    val endTime: Date?,
    val durationMinutes: Int,
    val isCompleted: Boolean,
    val focusTimeMinutes: Int,
    val breakTimeMinutes: Int,
    val cycles: Int
)