package com.example.focuslink.view.notifications.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.focuslink.view.notifications.domain.NotificationUseCase

class NotificationsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            return NotificationsViewModel(
                notificationUseCase = NotificationUseCase(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
