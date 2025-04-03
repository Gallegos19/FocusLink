package com.example.focuslink.core.theme

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton que gestiona el estado del tema en toda la aplicación
 */
object ThemeManager {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private const val THEME_PREFERENCES = "theme_preferences"
    private const val KEY_DARK_MODE = "dark_mode_enabled"
    private var preferences: SharedPreferences? = null

    /**
     * Inicializa el ThemeManager con el contexto de la aplicación
     * Debe llamarse al inicio de la aplicación (por ejemplo, en una clase Application personalizada)
     */
    fun init(context: Context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
            // Cargar el valor guardado
            val savedDarkMode = preferences?.getBoolean(KEY_DARK_MODE, false) ?: false
            _isDarkTheme.value = savedDarkMode
        }
    }

    /**
     * Cambia el tema de la aplicación y guarda la preferencia
     */
    fun setDarkTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark

        // Guardar la preferencia
        preferences?.edit()?.apply {
            putBoolean(KEY_DARK_MODE, isDark)
            apply() // apply() es asíncrono, use commit() si necesita que sea síncrono
        }
    }
}