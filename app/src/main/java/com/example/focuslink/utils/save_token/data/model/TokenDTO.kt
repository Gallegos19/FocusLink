package com.example.focuslink.utils.save_token.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenDTO(
    private val id: String,
    private val token: String
)
