package com.example.focuslink.view.settings.data.model

import android.provider.ContactsContract.CommonDataKinds.Email

data class UserResponse(
    val id:String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val age: Int,
    val createdAt: String
)
