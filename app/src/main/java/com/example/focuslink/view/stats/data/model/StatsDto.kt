package com.example.focuslink.view.stats.data.model

data class StatsDto(
    val userId: String,
    val totalCycles: Int,
    val totalFocusTime: Int, // en minutos
    val dailyStats: List<DailyStat>
)

data class DailyStat(
    val date: String,
    val focusTime: String,
    val cycles: Int
)