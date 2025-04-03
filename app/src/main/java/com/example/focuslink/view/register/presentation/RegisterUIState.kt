package com.example.focuslink.view.register.presentation

data class RegisterUIState(
    val username: String = "",
    val lastname: String = "",
    val age: Int = 0,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isRegistered: Boolean = false,
    val errorMessage: String = ""
)