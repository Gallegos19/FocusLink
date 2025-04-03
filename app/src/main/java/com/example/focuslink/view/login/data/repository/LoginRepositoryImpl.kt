package com.example.focuslink.view.login.data.repository

import android.annotation.SuppressLint
import com.example.focuslink.core.network.RetrofitHelper
import com.example.focuslink.view.login.data.datasource.LoginService
import com.example.focuslink.view.login.data.model.LoginRequest
import com.example.focuslink.view.login.data.model.LoginResponse

class LoginRepositoryImpl {
    @SuppressLint("SuspiciousIndentation")
    suspend fun LoginUser(user: LoginRequest): Result<LoginResponse> {
        val loginService = RetrofitHelper.getLoginService()

        return try {
            val response = loginService.login(user)
            if (response.isSuccessful) {
                val userDTO = response.body()
                if (userDTO != null) {
                    Result.success(userDTO)
                } else {
                    Result.failure(Exception("Cuerpo de respuesta vacío"))
                }
            } else {
                Result.failure(Exception("Error en la respuesta: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
