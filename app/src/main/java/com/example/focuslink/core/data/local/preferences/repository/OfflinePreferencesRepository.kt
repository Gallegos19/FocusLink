package com.example.focuslink.core.data.local.preferences.repository

import com.example.focuslink.core.data.local.preferences.dao.PreferencesDao
import com.example.focuslink.core.data.local.preferences.entities.PreferencesEntity

class OfflinePreferencesRepository(private val dao: PreferencesDao) {
    suspend fun save(prefs: PreferencesEntity) = dao.insert(prefs)
    suspend fun getByUser(userId: String) = dao.getByUser(userId)
    suspend fun clear() = dao.clear()
}
