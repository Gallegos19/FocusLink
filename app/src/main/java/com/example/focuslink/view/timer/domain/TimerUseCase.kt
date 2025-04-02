package com.example.focuslink.view.timer.domain

import com.example.focuslink.view.timer.data.model.SessionDto
import com.example.focuslink.view.timer.data.repository.SessionRepositoryImpl
import java.util.Date

class TimerUseCase(
    private val sessionRepository: SessionRepository = SessionRepositoryImpl()
) {
    private var currentSessionId: String? = null

    suspend fun startTimer(
        token: String,
        userId: String,
        focusTimeMinutes: Int,
        breakTimeMinutes: Int
    ): Result<SessionDto> {
        return try {
            val result = sessionRepository.startSession(
                token = token,
                userId = userId,
                startTime = Date(),
                focusTimeMinutes = focusTimeMinutes,
                breakTimeMinutes = breakTimeMinutes
            )

            if (result.isSuccess) {
                currentSessionId = result.getOrNull()?.id
            }

            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun stopTimer(
        token: String,
        sessionDto: SessionDto
    ): Result<SessionDto> {
        return try {
            if (currentSessionId == null) {
                return Result.failure(IllegalStateException("No hay una sesión activa"))
            }

            val result = sessionRepository.updateSession(
                token = token,
                sessionId = currentSessionId!!,
                sessionDto = sessionDto
            )

            if (result.isSuccess) {
                currentSessionId = null
            }

            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserSessions(
        token: String,
        userId: String
    ): Result<List<SessionDto>> {
        return try {
            sessionRepository.getUserSessions(token, userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}