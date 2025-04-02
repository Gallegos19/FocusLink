package com.example.focuslink.view.settings.domain

import android.content.Context
import com.example.focuslink.view.settings.data.model.PreferencesDto
import com.example.focuslink.view.settings.data.repository.PreferencesRepositoryImpl

class PreferencesUseCase(
    private val preferencesRepository: PreferencesRepository = PreferencesRepositoryImpl(),
    private val context: Context? = null
) {

    fun getPreferences(): Result<PreferencesDto> {
        return preferencesRepository.getPreferences()
    }

    fun savePreferences(
        focusTimeMinutes: Int,
        breakTimeMinutes: Int,
        notificationsEnabled: Boolean,
        soundEnabled: Boolean,
        vibrateEnabled: Boolean,
        darkModeEnabled: Boolean
    ): Result<Unit> {
        // Obtener preferencias actuales para mantener datos del usuario
        val currentPrefs = preferencesRepository.getPreferences()

        return if (currentPrefs.isSuccess) {
            val prefs = currentPrefs.getOrNull()
            prefs?.let {
                val updatedPrefs = it.copy(
                    focusTimeMinutes = focusTimeMinutes,
                    breakTimeMinutes = breakTimeMinutes,
                    notificationsEnabled = notificationsEnabled,
                    soundEnabled = soundEnabled,
                    vibrateEnabled = vibrateEnabled,
                    darkModeEnabled = darkModeEnabled
                )

                preferencesRepository.savePreferences(updatedPrefs)
            } ?: Result.failure(IllegalStateException("No se pudieron obtener las preferencias actuales"))
        } else {
            currentPrefs.exceptionOrNull()?.let {
                Result.failure(it)
            } ?: Result.failure(IllegalStateException("Error desconocido"))
        }
    }

    fun clearUserData(): Result<Unit> {
        return preferencesRepository.clearUserData()
    }
}