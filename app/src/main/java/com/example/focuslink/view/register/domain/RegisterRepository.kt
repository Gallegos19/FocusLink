package com.example.focuslink.view.register.domain

import com.example.focuslink.view.register.data.model.RegisterResponse

interface RegisterRepository {
    suspend fun register(username: String, email: String, password: String): Result<RegisterResponse>
}