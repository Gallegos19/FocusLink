package com.example.focuslink.view.login.domain

import com.example.focuslink.view.login.data.model.LoginResponse
import com.example.focuslink.view.login.data.repository.LoginRepositoryImpl

class LoginUseCase(
    private val loginRepository: LoginRepository = LoginRepositoryImpl()
) {
    suspend fun execute(email: String, password: String): Result<LoginResponse> {
        // Validación básica
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("El email no puede estar vacío"))
        }

        if (!isValidEmail(email)) {
            return Result.failure(IllegalArgumentException("El email no tiene un formato válido"))
        }

        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("La contraseña no puede estar vacía"))
        }

        return loginRepository.login(email, password)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }
}