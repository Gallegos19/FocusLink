package com.example.focuslink.view.notifications.data.repository

import android.util.Log
import com.example.focuslink.view.notifications.data.model.NotificationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Repositorio para manejar las notificaciones capturadas en memoria
 * Nota: Esta implementación es solo para uso temporal o de respaldo.
 * La implementación principal debería usar Room para persistir los datos.
 */
class NotificationMemoryRepository {
    private val tag = "NotificationMemoryRepo"

    // Almacenamiento en memoria de las notificaciones
    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: Flow<List<NotificationEntity>> = _notifications.asStateFlow()

    /**
     * Agrega una nueva notificación al repositorio
     */
    fun addNotification(notification: NotificationEntity) {
        _notifications.update { currentList ->
            // Verificar si ya existe una notificación similar (por ID o combinación de package y título)
            val exists = currentList.any { it.id == notification.id ||
                    (it.packageName == notification.packageName &&
                            it.title == notification.title &&
                            it.message == notification.message) }

            if (exists) {
                Log.d(tag, "Notificación ya existe, actualizando: ${notification.title}")
                // Actualizar la notificación existente
                currentList.map {
                    if (it.id == notification.id ||
                        (it.packageName == notification.packageName &&
                                it.title == notification.title &&
                                it.message == notification.message)) {
                        notification
                    } else {
                        it
                    }
                }
            } else {
                Log.d(tag, "Agregando nueva notificación: ${notification.title}")
                // Agregar al principio para mantener orden cronológico inverso
                listOf(notification) + currentList
            }
        }
    }

    /**
     * Marca una notificación como leída
     */
    fun markAsRead(notificationId: String) {
        _notifications.update { currentList ->
            currentList.map {
                if (it.id == notificationId) {
                    it.copy(isRead = true)
                } else {
                    it
                }
            }
        }
    }

    /**
     * Elimina una notificación por su ID
     */
    fun removeNotification(notificationId: String) {
        _notifications.update { currentList ->
            currentList.filter { it.id != notificationId }
        }
    }

    /**
     * Elimina todas las notificaciones
     */
    fun clearAllNotifications() {
        _notifications.value = emptyList()
    }
}