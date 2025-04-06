package com.example.focuslink.core.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object TimerStateManager {
    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private const val PREF_NAME = "focuslink_prefs"
    private const val KEY_TIMER_RUNNING = "timer_running"

    fun initialize(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        _isTimerRunning.value = sharedPreferences.getBoolean(KEY_TIMER_RUNNING, false)
    }

    fun setTimerRunning(context: Context, isRunning: Boolean) {
        _isTimerRunning.value = isRunning
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_TIMER_RUNNING, isRunning).apply()
    }

    fun isTimerRunning(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        Log.d("timer", _isTimerRunning.value.toString())
        return sharedPreferences.getBoolean(KEY_TIMER_RUNNING, false)
    }
}
