package com.example.focuslink.view.login.data.repository

import com.example.focuslink.core.network.RetrofitHelper
import com.example.focuslink.view.login.data.datasource.LoginService
import com.example.focuslink.view.login.data.model.LoginRequest
import com.example.focuslink.view.login.data.model.LoginResponse
import com.example.focuslink.view.login.domain.LoginRepository

class LoginRepositoryImpl(
    private val loginService: LoginService = RetrofitHelper.getLoginService()
) : LoginRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = loginService.login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}