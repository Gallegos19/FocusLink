package com.example.focuslink.view.register.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.view.register.data.model.RegisterRequest
import com.example.focuslink.view.register.domain.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val registerUseCase = RegisterUseCase()

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updateLastname(lastname: String) {
        _uiState.update { it.copy(lastname = lastname) }
    }

    // ✅ Ahora recibe un string y lo valida
    fun updateAge(ageInput: String) {
        val age = ageInput.toIntOrNull()

        if (age == null || age < 0 || age > 150) {
            _uiState.update {
                it.copy(
                    errorMessage = "Edad inválida. Debe estar entre 0 y 150.",
                    age = 0
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    age = age,
                    errorMessage = "" // limpia si está bien
                )
            }
        }
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

            val state = uiState.value

            if (state.username.isEmpty() || state.lastname.isEmpty() || state.age == 0 ||
                state.email.isEmpty() || state.password.isEmpty() || state.confirmPassword.isEmpty()) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Todos los campos son obligatorios",
                        isLoading = false
                    )
                }
                return@launch
            }

            if (state.password != state.confirmPassword) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Las contraseñas no coinciden",
                        isLoading = false
                    )
                }
                return@launch
            }

            try {
                val result = registerUseCase.execute(
                    RegisterRequest(
                        firstName = state.username,
                        lastName = state.lastname,
                        email = state.email,
                        password = state.password,
                        age = state.age
                    )
                )

                result.onSuccess {
                    _uiState.update {
                        it.copy(isRegistered = true, isLoading = false)
                    }
                }.onFailure {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Ocurrió un error al registrarte",
                            isLoading = false
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error inesperado: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }
}
