package com.example.focuslink.view.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.view.notifications.domain.NotificationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val notificationUseCase: NotificationUseCase = NotificationUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUIState())
    val uiState: StateFlow<NotificationsUIState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val result = notificationUseCase.getNotifications()

                if (result.isSuccess) {
                    val notifications = result.getOrNull()
                    _uiState.update {
                        it.copy(
                            notifications = notifications ?: emptyList(),
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.exceptionOrNull()?.message ?: "Error al cargar notificaciones",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Error desconocido",
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
                    // Actualizar la notificación en la UI
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
        loadNotifications()
    }
}