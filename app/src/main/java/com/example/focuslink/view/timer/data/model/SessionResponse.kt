package com.example.focuslink.view.timer.data.model

import java.util.Date

data class SessionResponse(
    val id: String,
    val userId: String,
    val startTime: Date,
    val endTime: Date,
    val type: String,
    val wasInterrumped: Boolean,
    val interrupted: List<String>

    )
