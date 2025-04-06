package com.example.focuslink.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.focuslink.core.data.local.preferences.dao.PreferencesDao
import com.example.focuslink.core.data.local.preferences.entities.PreferencesEntity
import com.example.focuslink.core.data.local.session.dao.SessionDao
import com.example.focuslink.core.data.local.session.entities.SessionEntity
import com.example.focuslink.core.data.local.user.dao.UserDao
import com.example.focuslink.core.data.local.user.entities.UserEntity
import com.example.focuslink.view.notifications.data.datasource.NotificationDao
import com.example.focuslink.view.notifications.data.model.NotificationEntity
import com.example.focuslink.view.notifications.data.model.NotificationEntity2

@Database(
    entities = [
        SessionEntity::class,
        UserEntity::class,
        PreferencesEntity::class,
        NotificationEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun userDao(): UserDao
    abstract fun preferencesDao(): PreferencesDao
    abstract fun notificationDao(): NotificationDao
}
