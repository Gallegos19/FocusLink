package com.example.focuslink.core.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.focuslink.R

class TimerForegroundService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val timeLeft = intent?.getStringExtra("timeLeft") ?: "00:00"
        val progress = intent?.getFloatExtra("progress", 1f) ?: 1f

        val notification = NotificationCompat.Builder(this, "focus_timer_channel")
            .setContentTitle("⏱ Tiempo de enfoque")
            .setContentText("Tiempo restante: $timeLeft")
            .setSmallIcon(R.drawable.focuslinklogo) // Asegúrate que este ícono exista
            .setProgress(100, (progress * 100).toInt(), false)
            .setOngoing(true)
            .build()

        startForeground(1, notification)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?) = null
}
