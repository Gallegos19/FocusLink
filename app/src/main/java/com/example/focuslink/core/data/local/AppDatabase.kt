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

@Database(
    entities = [
        SessionEntity::class,
        UserEntity::class,
        PreferencesEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun userDao(): UserDao
    abstract fun preferencesDao(): PreferencesDao
}
