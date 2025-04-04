package com.example.focuslink.view.stats.presentation


data class StatsUIState(
    val totalCycles: Int = 0,
    val breakCount: Int = 0,
    val longBreakCount: Int = 0,
    val totalHours: Int = 0,
    val totalMinutes: Int = 0,
    val totalInterruptions: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)