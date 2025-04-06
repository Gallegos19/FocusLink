package com.example.focuslink.view.notifications.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity2(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val packageName: String,
    val isRead: Boolean,
    val notificationKey: String,
    val createdAt: Long
)
