package com.example.focuslink.view.timer.data.repository

import com.example.focuslink.core.network.RetrofitHelper
import com.example.focuslink.view.timer.data.datasource.SessionService
import com.example.focuslink.view.timer.data.model.SessionDto
import com.example.focuslink.view.timer.data.model.SessionRequest
import com.example.focuslink.view.timer.data.model.SessionResponse
import com.example.focuslink.view.timer.domain.SessionRepository
import java.util.Date

class SessionRepositoryImpl {
    private val sessionService = RetrofitHelper.getSessionService()

    suspend fun startSession(token:String,session: SessionRequest): Result<SessionResponse> {
        return try {

            val response = sessionService.startSession("Bearer $token", session)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                }?:Result.failure(Exception("Respuesta erronea por parte del servidor"))
            }else{
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateSession(
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

    suspend fun getUserSessions(
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