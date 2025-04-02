package com.example.focuslink.view.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.view.login.domain.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase = LoginUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = "") }

            // Para demo, simulamos login exitoso después de validación básica
            if (uiState.value.email.isEmpty() || uiState.value.password.isEmpty()) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Email y contraseña son obligatorios",
                        isLoading = false
                    )
                }
                return@launch
            }

            // En implementación real, usaríamos loginUseCase.execute()
            // Simulamos respuesta exitosa para propósitos de UI
            _uiState.update {
                it.copy(
                    isLoggedIn = true,
                    isLoading = false
                )
            }
        }
    }
}