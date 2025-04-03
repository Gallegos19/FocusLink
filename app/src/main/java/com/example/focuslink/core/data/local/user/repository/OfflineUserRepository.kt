package com.example.focuslink.core.data.local.user.repository

import com.example.focuslink.core.data.local.user.dao.UserDao
import com.example.focuslink.core.data.local.user.entities.UserEntity

class OfflineUserRepository(private val dao: UserDao) {
    suspend fun save(user: UserEntity) = dao.insert(user)
    suspend fun getById(id: String) = dao.getById(id)
    suspend fun clear() = dao.clear()
}
