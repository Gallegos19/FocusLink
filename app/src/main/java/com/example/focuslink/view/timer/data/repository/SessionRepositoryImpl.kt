package com.example.focuslink.view.timer.data.repository

import com.example.focuslink.core.network.RetrofitHelper
import com.example.focuslink.view.timer.data.datasource.SessionService
import com.example.focuslink.view.timer.data.model.SessionDto
import com.example.focuslink.view.timer.data.model.SessionRequest
import com.example.focuslink.view.timer.domain.SessionRepository
import java.util.Date

class SessionRepositoryImpl(
    private val sessionService: SessionService = RetrofitHelper.getSessionService()
) : SessionRepository {

    override suspend fun startSession(
        token: String,
        userId: String,
        startTime: Date,
        focusTimeMinutes: Int,
        breakTimeMinutes: Int
    ): Result<SessionDto> {
        return try {
            val request = SessionRequest(
                userId = userId,
                startTime = startTime,
                focusTimeMinutes = focusTimeMinutes,
                breakTimeMinutes = breakTimeMinutes
            )

            val response = sessionService.startSession("Bearer $token", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateSession(
        token: String,
        sessionId: String,
        sessionDto: SessionDto
    ): Result<SessionDto> {
        return try {
            val response = sessionService.updateSession("Bearer $token", sessionId, sessionDto)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserSessions(
        token: String,
        userId: String
    ): Result<List<SessionDto>> {
        return try {
            val response = sessionService.getUserSessions("Bearer $token", userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}