package com.example.focuslink.view.notifications.data.repository

import com.example.focuslink.view.notifications.data.datasource.NotificationDao
import com.example.focuslink.view.notifications.data.model.NotificationEntity
import com.example.focuslink.view.notifications.domain.NotificationRepository
import kotlinx.coroutines.flow.Flow

class NotificationRepositoryImpl(
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override fun getAllNotifications(): Flow<List<NotificationEntity>> {
        return notificationDao.getAllNotifications()
    }

    override fun getUnreadNotifications(): Flow<List<NotificationEntity>> {
        return notificationDao.getUnreadNotifications()
    }

    override suspend fun insertNotification(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }

    override suspend fun updateNotification(notification: NotificationEntity) {
        notificationDao.updateNotification(notification)
    }

    override suspend fun markAsRead(notificationId: String) {
        notificationDao.markAsRead(notificationId)
    }

    override suspend fun deleteNotification(notification: NotificationEntity) {
        notificationDao.deleteNotification(notification)
    }

    override suspend fun deleteAllNotifications() {
        notificationDao.deleteAllNotifications()
    }
}