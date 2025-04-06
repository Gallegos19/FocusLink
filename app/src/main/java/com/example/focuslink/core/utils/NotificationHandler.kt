package com.example.focuslink.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.example.focuslink.MyApp

/**
 * Utilidad para gestionar permisos de notificaciones
 */
object NotificationPermissionHandler {

    /**
     * Verifica si la app tiene permiso para acceder a las notificaciones
     */
    fun isNotificationListenerEnabled(context: Context): Boolean {
        val packageName = context.packageName
        val flat = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )

        return flat?.contains(packageName) == true
    }

    /**
     * Abre la configuración de permisos de notificaciones
     */
    fun openNotificationListenerSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        activity.startActivity(intent)
    }

    /**
     * Solicita permiso de escucha de notificaciones con un mensaje explicativo
     */
    fun requestNotificationListenerPermission(activity: Activity) {
        if (!isNotificationListenerEnabled(activity)) {
            Toast.makeText(
                activity,
                "Para capturar notificaciones durante el tiempo de enfoque, " +
                        "necesitamos acceso a tus notificaciones. Por favor, activa el permiso para FocusLink.",
                Toast.LENGTH_LONG
            ).show()

            openNotificationListenerSettings(activity)
        }
    }

    /**
     * Comprueba y solicita todos los permisos necesarios para notificaciones
     * incluyendo el permiso de publicar notificaciones en Android 13+
     */
    fun checkAndRequestAllNotificationPermissions(activity: MyApp) {
        // Primero verificamos si tenemos permiso para publicar notificaciones
        val notificationManager = NotificationManagerCompat.from(activity)

        if (!notificationManager.areNotificationsEnabled()) {
            Toast.makeText(
                activity,
                "Para el correcto funcionamiento de FocusLink, " +
                        "por favor permite que la app envíe notificaciones.",
                Toast.LENGTH_LONG
            ).show()

            // Abrir configuración de notificaciones de la app
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
            activity.startActivity(intent)
        }

        // Luego verificamos el permiso para escuchar notificaciones
    }
}