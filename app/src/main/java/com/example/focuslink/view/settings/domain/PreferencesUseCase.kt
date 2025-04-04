package com.example.focuslink.view.settings.domain

import android.content.Context
import com.example.focuslink.view.settings.data.model.PreferencesDto
import com.example.focuslink.view.settings.data.model.PreferencesRequest
import com.example.focuslink.view.settings.data.model.PreferencesResponse
import com.example.focuslink.view.settings.data.model.UserResponse
import com.example.focuslink.view.settings.data.repository.PreferencesRepositoryImpl

class PreferencesUseCase() {
    private val preferencesRepository = PreferencesRepositoryImpl()
    private val context: Context? = null

    suspend fun getPreferences(token: String): Result<PreferencesResponse> {
        return preferencesRepository.getPreferences(token)
    }

    suspend fun savePreferences(token: String, preference: PreferencesRequest): Result<PreferencesResponse> {
        return preferencesRepository.savePreferences(token, preference)
    }

    suspend fun getUserById(token: String): Result<UserResponse> {
        return preferencesRepository.getUserById(token)
    }
}