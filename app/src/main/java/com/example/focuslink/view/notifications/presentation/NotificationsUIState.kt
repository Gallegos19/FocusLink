package com.example.focuslink.view.notifications.presentation

import com.example.focuslink.view.notifications.data.model.NotificationEntity

data class NotificationsUIState(
    val notifications: List<NotificationEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)