package com.example.focuslink.core.services

import android.app.Notification
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import com.example.focuslink.core.data.local.AppContainer
import com.example.focuslink.core.data.TimerStateManager
import com.example.focuslink.view.notifications.data.model.NotificationEntity
import com.example.focuslink.view.notifications.domain.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale
import java.util.UUID

class FocusLinkNotificationListener : NotificationListenerService() {

    private val tag = "NotificationListener"
    private lateinit var notificationRepository: NotificationRepository
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        notificationRepository = AppContainer(applicationContext).notificationRepository
        Log.d(tag, "Servicio de notificaciones iniciado")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // No procesar notificaciones propias de la app
        if (sbn.packageName == packageName) return

        val isRunning = TimerStateManager.isTimerRunning(applicationContext)
        if (!isRunning) {
            Log.d(tag, "Timer no está activo (SharedPreferences), ignorando notificación")
            return
        }


        try {
            val notification = sbn.notification

            // Extraer información de la notificación
            val extras = notification.extras

            val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
            val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
            val subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT)?.toString() ?: ""

            // Si no hay título ni texto, probablemente no es una notificación que queremos guardar
            if (title.isEmpty() && text.isEmpty()) return

            // Obtener información de la app
            val appName = getAppName(sbn.packageName)
            val appIcon = getAppIcon(sbn.packageName)

            // Crear timestamp
            val timestamp = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(sbn.postTime))

            // Crear entidad de notificación
            val notificationEntity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationEntity(
                    id = UUID.randomUUID().toString(),
                    packageName = sbn.packageName,
                    appName = appName,
                    title = title,
                    message = text,
                    timestamp = timestamp,
                    createdAt = LocalDateTime.now(),
                    isRead = false,
                    iconBitmap = appIcon
                )
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            // Guardar la notificación
            coroutineScope.launch {
                try {
                    notificationRepository.insertNotification(notificationEntity)
                    Log.d(tag, "Notificación guardada: $appName - $title")
                } catch (e: Exception) {
                    Log.e(tag, "Error guardando notificación: ${e.message}")
                }
            }

        } catch (e: Exception) {
            Log.e(tag, "Error procesando notificación: ${e.message}")
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Opcionalmente, puedes implementar lógica para eliminar notificaciones
        // cuando son removidas del sistema
    }

    private fun getAppName(packageName: String): String {
        val packageManager = applicationContext.packageManager
        return try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName // Si no se puede obtener el nombre, usar el package name
        }
    }

    private fun getAppIcon(packageName: String): Bitmap? {
        val packageManager = applicationContext.packageManager
        return try {
            val drawable: Drawable = packageManager.getApplicationIcon(packageName)
            drawable.toBitmap()
        } catch (e: Exception) {
            Log.e(tag, "Error obteniendo ícono para $packageName: ${e.message}")
            null
        }
    }
}