package com.example.focuslink.view.register.data.repository

import com.example.focuslink.core.network.RetrofitHelper
import com.example.focuslink.view.register.data.datasource.RegisterService
import com.example.focuslink.view.register.data.model.RegisterRequest
import com.example.focuslink.view.register.data.model.RegisterResponse
import com.example.focuslink.view.register.domain.RegisterRepository

class RegisterRepositoryImpl() {
    private val registerService = RetrofitHelper.getRegisterService()

    suspend fun register(user: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = registerService.register(user)
            if (response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception(response.errorBody()?.string()))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}