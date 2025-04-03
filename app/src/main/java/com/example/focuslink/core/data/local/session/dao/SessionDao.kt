package com.example.focuslink.core.data.local.session.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.focuslink.core.data.local.session.entities.SessionEntity

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: SessionEntity)

    @Query("SELECT * FROM session WHERE userId = :userId ORDER BY startTime DESC")
    suspend fun getSessionsByUser(userId: String): List<SessionEntity>

    @Query("DELETE FROM session WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM session")
    suspend fun clearAll()
}
