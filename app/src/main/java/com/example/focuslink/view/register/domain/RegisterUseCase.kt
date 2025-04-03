package com.example.focuslink.view.register.domain

import com.example.focuslink.view.register.data.model.RegisterRequest
import com.example.focuslink.view.register.data.model.RegisterResponse
import com.example.focuslink.view.register.data.repository.RegisterRepositoryImpl

class RegisterUseCase() {
    private val registerRepository = RegisterRepositoryImpl()
    suspend fun execute(user: RegisterRequest): Result<RegisterResponse> {
        // Validación básica
        if (user.firstName.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre de usuario no puede estar vacío"))
        }

        if (user.email.isBlank()) {
            return Result.failure(IllegalArgumentException("El email no puede estar vacío"))
        }

        if (!isValidEmail(user.email)) {
            return Result.failure(IllegalArgumentException("El email no tiene un formato válido"))
        }

        if (user.password.isBlank()) {
            return Result.failure(IllegalArgumentException("La contraseña no puede estar vacía"))
        }

        if (user.password.length < 6) {
            return Result.failure(IllegalArgumentException("La contraseña debe tener al menos 6 caracteres"))
        }

        return registerRepository.register(user)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }
}