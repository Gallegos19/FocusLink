package com.example.focuslink.view.settings.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.core.data.GlobalStorage
import com.example.focuslink.core.data.SessionManager
import com.example.focuslink.core.data.local.preferences.dao.PreferencesDao
import com.example.focuslink.core.data.local.preferences.entities.PreferencesEntity
import com.example.focuslink.core.data.local.preferences.repository.OfflinePreferencesRepository
import com.example.focuslink.core.theme.ThemeManager
import com.example.focuslink.view.settings.data.model.PreferencesRequest
import com.example.focuslink.view.settings.data.model.PreferencesResponse
import com.example.focuslink.view.settings.domain.PreferencesUseCase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesUseCase: PreferencesUseCase = PreferencesUseCase(),
    private val offlinePreferencesRepository: OfflinePreferencesRepository? = null
) : ViewModel() {
    val token = SessionManager.getToken()
    val bearerToken = "Bearer $token"
    private val _uiState = MutableStateFlow(SettingsUIState())
    val uiState: StateFlow<SettingsUIState> = _uiState.asStateFlow()

    init {
        loadPreferences()
        // Sincronizar el estado inicial desde ThemeManager
        viewModelScope.launch {
            ThemeManager.isDarkTheme.collect { isDarkTheme ->
                _uiState.update { it.copy(darkModeEnabled = isDarkTheme) }
            }
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Intentar cargar datos locales primero
                val userId = SessionManager.getUserId() ?: GlobalStorage.getUserId()
                if (userId != null && offlinePreferencesRepository != null) {
                    val localPrefs = offlinePreferencesRepository.getByUser(userId)
                    if (localPrefs != null) {
                        Log.d("SettingsViewModel", "Loaded local preferences: $localPrefs")
                        updateUiFromLocalPreferences(localPrefs)
                    }
                }

                // Cargar datos desde la API
                Log.d("SettingsViewModel", "Loading preferences with token: $token")
                val userResult = preferencesUseCase.getUserById(bearerToken)
                val preferencesResult = preferencesUseCase.getPreferences(bearerToken)

                // Obtener objetos directamente de los resultados
                val user = userResult.getOrNull()
                val prefs = preferencesResult.getOrNull()

                Log.d("SettingsViewModel", "User: $user")
                Log.d("SettingsViewModel", "Preferences: $prefs")

                if (user != null && prefs != null) {
                    Log.d("SettingsViewModel", "Successfully found user and preferences data")

                    // Guardar ID de usuario en SessionManager y GlobalStorage por seguridad
                    if (user.id.isNotEmpty()) {
                        SessionManager.saveUserId(user.id)
                        GlobalStorage.saveUserId(user.id)
                    }

                    // Actualizar UI con datos del servidor
                    _uiState.update { currentState ->
                        currentState.copy(
                            username = "${user.firstName} ${user.lastName}",
                            email = user.email,
                            focusTimeMinutes = prefs.focusTime,
                            breakTimeMinutes = prefs.shortBreak,
                            longTimeMinutes = prefs.longBreak,
                            notificationsEnabled = prefs.vibration,
                            soundEnabled = prefs.sound,
                            vibrateEnabled = prefs.vibration,
                            isLoading = false,
                            errorMessage = ""
                        )
                    }

                    // Guardar datos en local
                    saveToLocalDatabase(user.id, prefs)
                } else {
                    // Si ya teníamos datos locales, solo quitamos el indicador de carga
                    if (userId != null && offlinePreferencesRepository?.getByUser(userId) != null) {
                        _uiState.update { it.copy(isLoading = false) }
                    } else {
                        val msg = buildString {
                            if (user == null) append("Usuario no encontrado. ")
                            if (prefs == null) append("Preferencias no encontradas.")
                        }
                        Log.e("SettingsViewModel", "Error: $msg")
                        _uiState.update {
                            it.copy(
                                errorMessage = msg.ifEmpty { "No se pudieron obtener preferencias o usuario." },
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Exception in loadPreferences", e)

                // Si hay error de red pero tenemos datos locales
                val userId = SessionManager.getUserId() ?: GlobalStorage.getUserId()
                if (userId != null && offlinePreferencesRepository != null) {
                    try {
                        val localPrefs = offlinePreferencesRepository.getByUser(userId)
                        if (localPrefs != null) {
                            updateUiFromLocalPreferences(localPrefs)
                            return@launch
                        }
                    } catch (localEx: Exception) {
                        Log.e("SettingsViewModel", "Error reading local prefs", localEx)
                    }
                }

                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Error desconocido",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun updateUiFromLocalPreferences(prefs: PreferencesEntity) {
        _uiState.update {
            it.copy(
                focusTimeMinutes = prefs.focusTime,
                breakTimeMinutes = prefs.shortBreak,
                longTimeMinutes = prefs.longBreak,
                notificationsEnabled = prefs.vibration,
                soundEnabled = prefs.sound,
                vibrateEnabled = prefs.vibration,
                darkModeEnabled = prefs.theme == "dark",
                isLoading = false
            )
        }
    }

    private suspend fun saveToLocalDatabase(userId: String, prefs: PreferencesResponse) {
        if (offlinePreferencesRepository != null) {
            try {
                Log.d("SettingsViewModel", "Saving preferences to local database for user: $userId")
                val prefsEntity = PreferencesEntity(
                    userId = userId,
                    focusTime = prefs.focusTime,
                    shortBreak = prefs.shortBreak,
                    longBreak = prefs.longBreak,
                    vibration = prefs.vibration,
                    sound = prefs.sound,
                    theme = prefs.theme
                )
                offlinePreferencesRepository.save(prefsEntity)
                Log.d("SettingsViewModel", "Preferences saved to local database")
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error saving to local database", e)
            }
        } else {
            Log.d("SettingsViewModel", "OfflinePreferencesRepository is null, cannot save to local database")
        }
    }

    fun solicitarTokenFCM() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("FCM", "Token FCM obtenido: $token")
                    // Aquí puedes guardar el token localmente o mandarlo a tu servidor
                } else {
                    Log.e("FCM", "Error al obtener token FCM", task.exception)
                }
            }
    }

    fun updateFocusTime(minutes: Int) {
        _uiState.update { it.copy(focusTimeMinutes = minutes) }
        savePreferences()
    }

    fun updateBreakTime(minutes: Int) {
        _uiState.update { it.copy(breakTimeMinutes = minutes) }
        savePreferences()
    }

    fun toggleNotifications(enabled: Boolean) {
        _uiState.update {
            it.copy(
                notificationsEnabled = enabled,
                soundEnabled = if (!enabled) false else it.soundEnabled,
                vibrateEnabled = if (!enabled) false else it.vibrateEnabled
            )
        }
        savePreferences()
    }

    fun toggleSound(enabled: Boolean) {
        _uiState.update { it.copy(soundEnabled = enabled) }
        savePreferences()
    }

    fun toggleVibrate(enabled: Boolean) {
        _uiState.update { it.copy(vibrateEnabled = enabled) }
        savePreferences()
    }

    fun toggleDarkMode(isDark: Boolean) {
        _uiState.update { it.copy(darkModeEnabled = isDark) }

        viewModelScope.launch {
            val userId = SessionManager.getUserId() ?: GlobalStorage.getUserId()
            if (userId != null) {
                savePreferences()
                ThemeManager.setDarkTheme(isDark) // <- Esto es lo que activa el cambio visual
            }
        }
    }


    private fun savePreferences() {
        viewModelScope.launch {
            val theme = if (_uiState.value.darkModeEnabled) "dark" else "light"
            val token = SessionManager.getToken()
            Log.d("token","token : ${bearerToken}")
            val preferences = PreferencesRequest(
                focusTime = _uiState.value.focusTimeMinutes,
                shortBreak = _uiState.value.breakTimeMinutes,
                longBreak = _uiState.value.longTimeMinutes,
                vibration = _uiState.value.vibrateEnabled,
                sound = _uiState.value.soundEnabled,
                theme = theme
            )
            try {
                val result = preferencesUseCase.savePreferences(bearerToken!!, preferences)
                if (result.isSuccess) {
                    // Si se guardó correctamente en la API, también guardar localmente
                    val prefs = result.getOrNull()
                    val userId = SessionManager.getUserId() ?: GlobalStorage.getUserId()
                    if (prefs != null && userId != null) {
                        saveToLocalDatabase(userId, prefs)
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.exceptionOrNull()?.message
                                ?: "Error al guardar preferencias"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                // Limpiamos la base de datos local al cerrar sesión
                offlinePreferencesRepository?.clear()

                _uiState.update {
                    it.copy(
                        logout = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Error al cerrar sesión"
                    )
                }
            }
        }
    }
}