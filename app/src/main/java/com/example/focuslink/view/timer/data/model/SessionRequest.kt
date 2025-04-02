package com.example.focuslink.view.timer.data.model

import java.util.Date

data class SessionRequest(
    val userId: String,
    val startTime: Date,
    val focusTimeMinutes: Int,
    val breakTimeMinutes: Int
)