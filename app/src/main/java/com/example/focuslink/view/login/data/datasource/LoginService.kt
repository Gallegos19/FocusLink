package com.example.focuslink.view.login.data.datasource

import com.example.focuslink.core.utils.Constants
import com.example.focuslink.view.login.data.model.LoginRequest
import com.example.focuslink.view.login.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST(Constants.LOGIN_ENDPOINT)
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}