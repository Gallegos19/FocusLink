package com.example.focuslink.view.timer.presentation

data class TimerUIState(
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val isBreakTime: Boolean = false,
    val currentCycle: Int = 1,
    val progress: Float = 1f,
    val timeLeftFormatted: String = "00:10"
)