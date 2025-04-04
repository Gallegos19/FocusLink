package com.example.focuslink.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.focuslink.core.services.TimerForegroundService

fun Context.startFocusNotification(timeLeft: String, progress: Float) {
    val intent = Intent(this, TimerForegroundService::class.java)
    intent.putExtra("timeLeft", timeLeft)
    intent.putExtra("progress", progress)
    ContextCompat.startForegroundService(this, intent)
}