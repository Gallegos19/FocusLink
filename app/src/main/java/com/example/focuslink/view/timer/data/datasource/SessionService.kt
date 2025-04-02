package com.example.focuslink.view.timer.data.datasource

import com.example.focuslink.core.utils.Constants
import com.example.focuslink.view.timer.data.model.SessionDto
import com.example.focuslink.view.timer.data.model.SessionRequest
import retrofit2.http.*

interface SessionService {
    @POST(Constants.SESSIONS_ENDPOINT)
    suspend fun startSession(
        @Header("Authorization") token: String,
        @Body sessionRequest: SessionRequest
    ): SessionDto

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