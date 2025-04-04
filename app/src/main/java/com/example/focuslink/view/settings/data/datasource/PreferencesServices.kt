package com.example.focuslink.view.settings.data.datasource

import com.example.focuslink.core.utils.Constants
import com.example.focuslink.view.login.data.model.LoginRequest
import com.example.focuslink.view.login.data.model.LoginResponse
import com.example.focuslink.view.settings.data.model.PreferencesRequest
import com.example.focuslink.view.settings.data.model.PreferencesResponse
import com.example.focuslink.view.settings.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface PreferencesServices {
    @POST("/preferences")
    suspend fun savePreferences(@Header("Authorization") token: String, @Body preference: PreferencesRequest): Response<PreferencesResponse>

    @GET("/preferences")
    suspend fun getPreferences(@Header("Authorization") token: String): Response<PreferencesResponse>

    @GET("/user")
    suspend fun getUserById(@Header("Authorization") token: String): Response<UserResponse>
}