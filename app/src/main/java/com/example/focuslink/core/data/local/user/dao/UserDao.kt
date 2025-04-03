package com.example.focuslink.core.data.local.user.dao

import androidx.room.*
import com.example.focuslink.core.data.local.user.entities.UserEntity
import com.example.focuslink.core.data.local.user.relations.UserWithSessions

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getById(id: String): UserEntity?

    @Query("DELETE FROM user")
    suspend fun clear()

    @Transaction
    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserWithSessions(userId: String): UserWithSessions?

}
