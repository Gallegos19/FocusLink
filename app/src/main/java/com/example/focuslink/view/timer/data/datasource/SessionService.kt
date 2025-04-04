package com.example.focuslink.view.timer.data.datasource

import com.example.focuslink.core.utils.Constants
import com.example.focuslink.view.timer.data.model.SessionDto
import com.example.focuslink.view.timer.data.model.SessionRequest
import com.example.focuslink.view.timer.data.model.SessionResponse
import retrofit2.Response
import retrofit2.http.*

interface SessionService {
    @POST("sessions")
    suspend fun startSession(
        @Header("Authorization") token: String,
        @Body sessionRequest: SessionRequest
    ): Response<SessionResponse>

    @PUT("${Constants.SESSIONS_ENDPOINT}/{sessionId}")
    suspend fun updateSession(
        @Header("Authorization") token: String,
        @Path("sessionId") sessionId: String,
        @Body sessionDto: SessionDto
    ): SessionDto

    @GET(Constants.SESSIONS_ENDPOINT)
    suspend fun getUserSessions(
        @Header("Authorization") token: String,
        @Query("userId") userId: String
    ): List<SessionDto>
}