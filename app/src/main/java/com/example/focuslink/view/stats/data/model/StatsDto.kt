package com.example.focuslink.view.stats.data.model

data class StatsDto(
    val focusCount: Int,
    val breakCount: Int,
    val longBreakCount: Int,
    val totalTime: Double,
    val totalInterruptions: Int
)