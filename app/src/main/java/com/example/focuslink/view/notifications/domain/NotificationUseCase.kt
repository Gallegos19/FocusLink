package com.example.focuslink.view.notifications.domain

import android.util.Log
import com.example.focuslink.core.data.local.AppContainer
import com.example.focuslink.view.notifications.data.model.NotificationEntity
import kotlinx.coroutines.flow.first
import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach

class NotificationUseCase(context: Context) {
    private val tag = "NotificationUseCase"
    private val notificationRepository = AppContainer(context).notificationRepository

    /**
     * Obtiene todas las notificaciones guardadas
     */
    suspend fun getNotifications(): Flow<List<NotificationEntity>> {
        return notificationRepository.getUnreadNotifications()
            .onEach {
                Log.d(tag, "Notificaciones NO LEÍDAS: ${it.size}")
            }
            .catch { e ->
                Log.e(tag, "Error al obtener notificaciones no leídas: ${e.message}")
                emit(emptyList())
            }
    }


    /**
     * Marca una notificación como leída
     */
    suspend fun markAsRead(notificationId: String): Result<Boolean> {
        return try {
            notificationRepository.markAsRead(notificationId)
            Log.d(tag, "Notificación marcada como leída: $notificationId")
            Result.success(true)
        } catch (e: Exception) {
            Log.e(tag, "Error marcando notificación como leída: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Elimina todas las notificaciones
     */
    suspend fun clearAllNotifications(): Result<Boolean> {
        return try {
            notificationRepository.deleteAllNotifications()
            Log.d(tag, "Todas las notificaciones eliminadas")
            Result.success(true)
        } catch (e: Exception) {
            Log.e(tag, "Error eliminando notificaciones: ${e.message}")
            Result.failure(e)
        }
    }
}