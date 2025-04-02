package com.example.focuslink.view.stats.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.view.stats.domain.StatsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatsViewModel(
    private val statsUseCase: StatsUseCase = StatsUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUIState())
    val uiState: StateFlow<StatsUIState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val result = statsUseCase.execute()

                if (result.isSuccess) {
                    val stats = result.getOrNull()
                    stats?.let { statsData ->
                        // Convertir minutos totales a horas y minutos
                        val totalHours = statsData.totalFocusTime / 60
                        val totalMinutes = statsData.totalFocusTime % 60

                        _uiState.update { currentState ->
                            currentState.copy(
                                totalCycles = statsData.totalCycles,
                                totalHours = totalHours,
                                totalMinutes = totalMinutes,
                                dailyStats = statsData.dailyStats,
                                isLoading = false
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.exceptionOrNull()?.message ?: "Error al cargar estadísticas",
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

    fun refreshStats() {
        loadStats()
    }
}