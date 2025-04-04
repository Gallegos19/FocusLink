package com.example.focuslink.view.timer.data.model

import java.util.Date

data class SessionRequest(
    val startTime: Date,
    val endTime: Date,
    val type: String,
    val wasInterrumped: Boolean,
    val interruptedBy: List<String>
)