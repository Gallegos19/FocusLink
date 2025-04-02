package com.example.focuslink.core.services

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.focuslink.core.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class NotificationListenerService : NotificationListenerService() {

    private lateinit var sharedPreferences: SharedPreferences
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    // Lista de paquetes para rastrear
    private val trackingPackages = listOf(
        "com.whatsapp",
        "com.facebook.orca",
        "com.instagram.android",
        "com.twitter.android"
    )

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Comprobar si el rastreo de notificaciones está habilitado
        val notificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true)
        if (!notificationsEnabled) return

        // Comprobar si la notificación es de una aplicación rastreada
        if (sbn.packageName !in trackingPackages) return

        // Obtener detalles de la notificación
        val notification = sbn.notification
        val extras = notification.extras

        val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""

        // Solo rastrear si tenemos contenido real
        if (title.isNotEmpty() || text.isNotEmpty()) {
            coroutineScope.launch {
                val appName = getApplicationLabel(sbn.packageName)

                // Aquí se procesaría la notificación
                // En una implementación real, guardaríamos en la base de datos
            }
        }
    }

    private fun getApplicationLabel(packageName: String): String {
        return try {
            val packageManager = applicationContext.packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: Exception) {
            packageName.split(".").last()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
}