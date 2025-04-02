package com.example.focuslink.view.timer.domain

import com.example.focuslink.view.timer.data.model.SessionDto
import java.util.Date

interface SessionRepository {
    suspend fun startSession(
        token: String,
        userId: String,
        startTime: Date,
        focusTimeMinutes: Int,
        breakTimeMinutes: Int
    ): Result<SessionDto>

    suspend fun updateSession(
        token: String,
        sessionId: String,
        sessionDto: SessionDto
    ): Result<SessionDto>

    suspend fun getUserSessions(
        token: String,
        userId: String
    ): Result<List<SessionDto>>
}