package com.example.focuslink.view.login.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val email:String,
    val name:String
)
