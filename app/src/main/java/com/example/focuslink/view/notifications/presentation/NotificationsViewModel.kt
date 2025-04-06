package com.example.focuslink.view.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.view.notifications.data.model.NotificationEntity
import com.example.focuslink.view.notifications.domain.NotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect


class NotificationsViewModel(
    private val notificationUseCase: NotificationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUIState())
    val uiState: StateFlow<NotificationsUIState> = _uiState.asStateFlow()

    init {
        observeUnreadNotifications()
    }

    private fun observeUnreadNotifications() {
        viewModelScope.launch {
            notificationUseCase.getNotifications()
                .collect { unreadNotifications ->
                    _uiState.update {
                        it.copy(
                            notifications = unreadNotifications,
                            isLoading = false
                        )
                    }
                }
        }
    }


    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            try {
                val result = notificationUseCase.markAsRead(notificationId)

                if (result.isSuccess) {
                    _uiState.update { currentState ->
                        val updatedNotifications = currentState.notifications.map { notification ->
                            if (notification.id == notificationId) {
                                notification.copy(isRead = true)
                            } else {
                                notification
                            }
                        }

                        currentState.copy(notifications = updatedNotifications)

                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Error al marcar como leída")
                }
            }
        }
    }

    fun refreshNotifications() {
        observeUnreadNotifications()
    }
}