package com.example.focuslink.view.register.data.datasource

import com.example.focuslink.core.utils.Constants
import com.example.focuslink.view.register.data.model.RegisterRequest
import com.example.focuslink.view.register.data.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST(Constants.REGISTER_ENDPOINT)
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse
}