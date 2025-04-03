package com.example.focuslink.view.register.data.model

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val age: Int
)