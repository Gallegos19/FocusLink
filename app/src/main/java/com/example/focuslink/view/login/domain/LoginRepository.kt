package com.example.focuslink.view.login.domain

import com.example.focuslink.view.login.data.model.LoginResponse

interface LoginRepository {
    suspend fun login(email: String, password: String): Result<LoginResponse>
}