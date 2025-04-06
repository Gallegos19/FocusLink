package com.example.focuslink.core.data.local

import android.content.Context
import androidx.room.Room
import com.example.focuslink.core.data.local.preferences.repository.OfflinePreferencesRepository
import com.example.focuslink.core.data.local.session.repository.OfflineSessionRepository
import com.example.focuslink.core.data.local.user.repository.OfflineUserRepository
import com.example.focuslink.view.notifications.data.repository.NotificationRepositoryImpl

class AppContainer(context: Context) {

    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "focuslink_db"
    )
        .fallbackToDestructiveMigration() // ⚠️ solo usar en desarrollo
        .build()

    val sessionRepository = OfflineSessionRepository(db.sessionDao())
    val userRepository = OfflineUserRepository(db.userDao())
    val preferencesRepository = OfflinePreferencesRepository(db.preferencesDao())
    val notificationDao = db.notificationDao()
    val notificationRepository = NotificationRepositoryImpl(notificationDao)
}