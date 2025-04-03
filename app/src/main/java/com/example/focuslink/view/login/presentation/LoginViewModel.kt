package com.example.focuslink.view.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.core.data.SessionManager
import com.example.focuslink.view.login.data.model.LoginRequest
import com.example.focuslink.view.login.data.model.LoginResponse
import com.example.focuslink.view.login.domain.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {
    private val LoginUseCase  = LoginUseCase()

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
            val result = LoginUseCase.execute(LoginRequest(_uiState.value.email, _uiState.value.password))
            result
                .onSuccess { response: LoginResponse ->
                // ✅ Guardar token o usuario en SessionManager
                SessionManager.saveToken(response.token) // Asegúrate que exista
                SessionManager.saveUserId(response.user.id) // Si quieres guardar también el ID
                _uiState.update {
                    it.copy(isLoggedIn = true, isLoading = false)
                }
            }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(errorMessage = error.message ?: "Error desconocido", isLoading = false)
                    }
                }

        }
    }
}