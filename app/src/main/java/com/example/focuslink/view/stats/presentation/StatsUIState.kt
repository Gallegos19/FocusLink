package com.example.focuslink.view.stats.presentation

import com.example.focuslink.view.stats.data.model.DailyStat

data class StatsUIState(
    val totalCycles: Int = 0,
    val totalHours: Int = 0,
    val totalMinutes: Int = 0,
    val dailyStats: List<DailyStat> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)