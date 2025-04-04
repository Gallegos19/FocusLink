package com.example.focuslink.view.timer.domain

import com.example.focuslink.view.timer.data.model.SessionRequest
import com.example.focuslink.view.timer.data.model.SessionResponse
import com.example.focuslink.view.timer.data.repository.SessionRepositoryImpl

class AddsSessionUseCase {
    private val repository = SessionRepositoryImpl()

    suspend fun addSession(token:String, session: SessionRequest): Result<SessionResponse>{
        val result = repository.startSession(token,session)
        return result
    }
}