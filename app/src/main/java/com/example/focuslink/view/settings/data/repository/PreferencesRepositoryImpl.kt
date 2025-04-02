package com.example.focuslink.view.settings.data.repository

import android.content.Context
import com.example.focuslink.view.settings.data.datasource.PreferencesService
import com.example.focuslink.view.settings.data.model.PreferencesDto
import com.example.focuslink.view.settings.domain.PreferencesRepository

class PreferencesRepositoryImpl(
    private val context: Context? = null,
    private val preferencesService: PreferencesService? = null
) : PreferencesRepository {

    override fun getPreferences(): Result<PreferencesDto> {
        return try {
            if (context == null && preferencesService == null) {
                // Mock para demo UI
                val mockPreferences = PreferencesDto(
                    userId = "mock-user-id",
                    username = "Usuario",
                    email = "usuario@example.com",
                    focusTimeMinutes = 25,
                    breakTimeMinutes = 5,
                    notificationsEnabled = true,
                    soundEnabled = true,
                    vibrateEnabled = true,
                    darkModeEnabled = false
                )
                Result.success(mockPreferences)
            } else {
                // Usar implementación real
                val service = preferencesService ?: PreferencesService(context!!)
                Result.success(service.getPreferences())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun savePreferences(preferences: PreferencesDto): Result<Unit> {
        return try {
            if (context == null && preferencesService == null) {
                // Mock para demo UI
                Result.success(Unit)
            } else {
                // Usar implementación real
                val service = preferencesService ?: PreferencesService(context!!)
                service.savePreferences(preferences)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun clearUserData(): Result<Unit> {
        return try {
            if (context == null && preferencesService == null) {
                // Mock para demo UI
                Result.success(Unit)
            } else {
                // Usar implementación real
                val service = preferencesService ?: PreferencesService(context!!)
                service.clearUserData()
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}