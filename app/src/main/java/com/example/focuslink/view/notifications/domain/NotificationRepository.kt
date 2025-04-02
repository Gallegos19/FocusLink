package com.example.focuslink.view.notifications.domain

import com.example.focuslink.view.notifications.data.model.NotificationEntity
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getAllNotifications(): Flow<List<NotificationEntity>>
    fun getUnreadNotifications(): Flow<List<NotificationEntity>>
    suspend fun insertNotification(notification: NotificationEntity)
    suspend fun updateNotification(notification: NotificationEntity)
    suspend fun markAsRead(notificationId: String)
    suspend fun deleteNotification(notification: NotificationEntity)
    suspend fun deleteAllNotifications()
}