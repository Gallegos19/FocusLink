package com.example.focuslink.core.data.local.session.repository

import com.example.focuslink.core.data.local.session.dao.SessionDao
import com.example.focuslink.core.data.local.session.entities.SessionEntity

class OfflineSessionRepository(private val dao: SessionDao) {
    suspend fun saveSession(session: SessionEntity) = dao.insert(session)

    suspend fun getUserSessions(userId: String): List<SessionEntity> =
        dao.getSessionsByUser(userId)

    suspend fun deleteSession(id: String) = dao.deleteById(id)

    suspend fun clearAll() = dao.clearAll()
}
