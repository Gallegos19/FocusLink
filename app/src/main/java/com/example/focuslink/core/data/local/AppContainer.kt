package com.example.focuslink.core.data.local

import android.content.Context
import androidx.room.Room
import com.example.focuslink.core.data.local.preferences.repository.OfflinePreferencesRepository
import com.example.focuslink.core.data.local.session.repository.OfflineSessionRepository
import com.example.focuslink.core.data.local.user.repository.OfflineUserRepository

class AppContainer(context: Context) {
    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "focuslink_db"
    ).build()

    val sessionRepository = OfflineSessionRepository(db.sessionDao())
    val userRepository = OfflineUserRepository(db.userDao())
    val preferencesRepository = OfflinePreferencesRepository(db.preferencesDao())
}