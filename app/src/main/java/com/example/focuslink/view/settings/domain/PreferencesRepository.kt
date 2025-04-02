package com.example.focuslink.view.settings.domain

import com.example.focuslink.view.settings.data.model.PreferencesDto

interface PreferencesRepository {
    fun getPreferences(): Result<PreferencesDto>
    fun savePreferences(preferences: PreferencesDto): Result<Unit>
    fun clearUserData(): Result<Unit>
}