package com.example.focuslink.view.notifications.data.model

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.focuslink.core.data.local.BitmapConverter
import com.example.focuslink.core.data.local.DateTimeConverter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity(tableName = "notifications")
@TypeConverters(BitmapConverter::class, DateTimeConverter::class)
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val packageName: String,
    val appName: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val createdAt: LocalDateTime? = null,
    val isRead: Boolean = false,
    val iconBitmap: Bitmap? = null
) {
    companion object {
        @SuppressLint("NewApi")
        private val MEXICO_ZONE = ZoneId.of("America/Mexico_City") // Más preciso que GMT-6

        @SuppressLint("NewApi")
        fun create(
            id: String,
            packageName: String,
            appName: String,
            title: String,
            message: String,
            timestamp: String,
            isRead: Boolean = false,
            iconBitmap: Bitmap? = null
        ): NotificationEntity {
            return NotificationEntity(
                id = id,
                packageName = packageName,
                appName = appName,
                title = title,
                message = message,
                timestamp = timestamp,
                createdAt = ZonedDateTime.now(MEXICO_ZONE).toLocalDateTime(),
                isRead = isRead,
                iconBitmap = iconBitmap
            )
        }
    }
}