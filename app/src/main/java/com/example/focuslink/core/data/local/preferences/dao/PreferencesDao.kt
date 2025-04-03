package com.example.focuslink.core.data.local.preferences.dao

import androidx.room.*
import com.example.focuslink.core.data.local.preferences.entities.PreferencesEntity

@Dao
interface PreferencesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preferences: PreferencesEntity)

    @Query("SELECT * FROM preferences WHERE userId = :userId")
    suspend fun getByUser(userId: String): PreferencesEntity?

    @Query("DELETE FROM preferences")
    suspend fun clear()
}
