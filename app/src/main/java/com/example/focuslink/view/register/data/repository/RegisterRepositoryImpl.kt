package com.example.focuslink.view.register.data.repository

import com.example.focuslink.core.network.RetrofitHelper
import com.example.focuslink.view.register.data.datasource.RegisterService
import com.example.focuslink.view.register.data.model.RegisterRequest
import com.example.focuslink.view.register.data.model.RegisterResponse
import com.example.focuslink.view.register.domain.RegisterRepository

class RegisterRepositoryImpl(
    private val registerService: RegisterService = RetrofitHelper.getRegisterService()
) : RegisterRepository {

    override suspend fun register(username: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val response = registerService.register(
                RegisterRequest(
                    username = username,
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