package com.example.focuslink.view.register.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.view.register.domain.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase = RegisterUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun register() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = "") }

            // Validaciones básicas para la UI
            if (uiState.value.username.isEmpty() ||
                uiState.value.email.isEmpty() ||
                uiState.value.password.isEmpty() ||
                uiState.value.confirmPassword.isEmpty()) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Todos los campos son obligatorios",
                        isLoading = false
                    )
                }
                return@launch
            }

            if (uiState.value.password != uiState.value.confirmPassword) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Las contraseñas no coinciden",
                        isLoading = false
                    )
                }
                return@launch
            }

            // En una implementación real, llamaríamos a registerUseCase.execute()
            // Para demo, simulamos registro exitoso
            _uiState.update {
                it.copy(
                    isRegistered = true,
                    isLoading = false
                )
            }
        }
    }
}