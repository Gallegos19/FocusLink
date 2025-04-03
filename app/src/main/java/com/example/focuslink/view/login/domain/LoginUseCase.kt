package com.example.focuslink.view.login.domain

import com.example.focuslink.view.login.data.model.LoginRequest
import com.example.focuslink.view.login.data.model.LoginResponse
import com.example.focuslink.view.login.data.repository.LoginRepositoryImpl

class LoginUseCase( ){
    private val repository = LoginRepositoryImpl()

    suspend fun execute(User: LoginRequest): Result<LoginResponse> {
        // Validación básica
        if (User.email.isBlank()) {
            return Result.failure(IllegalArgumentException("El email no puede estar vacío"))
        }

        if (!isValidEmail(User.email)) {
            return Result.failure(IllegalArgumentException("El email no tiene un formato válido"))
        }

        if (User.password.isBlank()) {
            return Result.failure(IllegalArgumentException("La contraseña no puede estar vacía"))
        }

        return repository.LoginUser(User)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }
}