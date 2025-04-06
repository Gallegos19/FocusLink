package com.example.focuslink.utils

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton para gestionar y compartir el estado del timer entre componentes
 */
object TimerStateManager {
    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    /**
     * Actualiza el estado del timer
     */
    fun setTimerRunning(isRunning: Boolean) {
        _isTimerRunning.value = isRunning
    }

    /**
     * Devuelve si el timer está activo actualmente
     */
    fun isTimerRunning(applicationContext: Context): Boolean {
        return _isTimerRunning.value
    }
}