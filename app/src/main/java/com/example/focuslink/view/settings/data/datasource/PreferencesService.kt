package com.example.focuslink.view.settings.data.datasource

import android.content.Context
import android.content.SharedPreferences
import com.example.focuslink.core.utils.Constants
import com.example.focuslink.view.settings.data.model.PreferencesDto

class PreferencesService(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        Constants.USER_PREFERENCES,
        Context.MODE_PRIVATE
    )

    fun getPreferences(): PreferencesDto {
        return PreferencesDto(
            userId = sharedPreferences.getString("user_id", "") ?: "",
            username = sharedPreferences.getString("username", "Usuario") ?: "Usuario",
            email = sharedPreferences.getString("email", "usuario@example.com") ?: "usuario@example.com",
            focusTimeMinutes = sharedPreferences.getInt("focus_time_minutes", Constants.DEFAULT_FOCUS_TIME_MINUTES),
            breakTimeMinutes = sharedPreferences.getInt("break_time_minutes", Constants.DEFAULT_SHORT_BREAK_MINUTES),
            notificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true),
            soundEnabled = sharedPreferences.getBoolean("sound_enabled", true),
            vibrateEnabled = sharedPreferences.getBoolean("vibrate_enabled", true),
            darkModeEnabled = sharedPreferences.getBoolean("dark_mode_enabled", false)
        )
    }

    fun savePreferences(preferences: PreferencesDto) {
        sharedPreferences.edit().apply {
            putString("user_id", preferences.userId)
            putString("username", preferences.username)
            putString("email", preferences.email)
            putInt("focus_time_minutes", preferences.focusTimeMinutes)
            putInt("break_time_minutes", preferences.breakTimeMinutes)
            putBoolean("notifications_enabled", preferences.notificationsEnabled)
            putBoolean("sound_enabled", preferences.soundEnabled)
            putBoolean("vibrate_enabled", preferences.vibrateEnabled)
            putBoolean("dark_mode_enabled", preferences.darkModeEnabled)
        }.apply()
    }

    fun clearUserData() {
        sharedPreferences.edit().apply {
            remove("user_id")
            remove("username")
            remove("email")
            remove(Constants.TOKEN_KEY)
        }.apply()
    }
}