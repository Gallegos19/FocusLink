package com.example.focuslink.view.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.view.settings.domain.PreferencesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesUseCase: PreferencesUseCase = PreferencesUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUIState())
    val uiState: StateFlow<SettingsUIState> = _uiState.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val preferencesResult = preferencesUseCase.getPreferences()

                if (preferencesResult.isSuccess) {
                    val preferences = preferencesResult.getOrNull()
                    preferences?.let { prefs ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                username = prefs.username,
                                email = prefs.email,
                                focusTimeMinutes = prefs.focusTimeMinutes,
                                breakTimeMinutes = prefs.breakTimeMinutes,
                                notificationsEnabled = prefs.notificationsEnabled,
                                soundEnabled = prefs.soundEnabled,
                                vibrateEnabled = prefs.vibrateEnabled,
                                darkModeEnabled = prefs.darkModeEnabled,
                                isLoading = false
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = preferencesResult.exceptionOrNull()?.message
                                ?: "Error al cargar preferencias",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Error desconocido",
                        isLoading = false
                    )
                }
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
        _uiState.update { it.copy(notificationsEnabled = enabled) }
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

    fun toggleDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(darkModeEnabled = enabled) }
        savePreferences()
    }

    private fun savePreferences() {
        viewModelScope.launch {
            try {
                val result = preferencesUseCase.savePreferences(
                    focusTimeMinutes = _uiState.value.focusTimeMinutes,
                    breakTimeMinutes = _uiState.value.breakTimeMinutes,
                    notificationsEnabled = _uiState.value.notificationsEnabled,
                    soundEnabled = _uiState.value.soundEnabled,
                    vibrateEnabled = _uiState.value.vibrateEnabled,
                    darkModeEnabled = _uiState.value.darkModeEnabled
                )

                if (result.isFailure) {
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
                preferencesUseCase.clearUserData()
                // La navegación a la pantalla de login debe hacerse desde la llamada
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