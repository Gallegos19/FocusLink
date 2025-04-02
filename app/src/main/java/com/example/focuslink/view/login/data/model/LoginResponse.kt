package com.example.focuslink.view.login.data.model

data class LoginResponse(
    val token: String,
    val userId: String,
    val email: String,
    val username: String
)