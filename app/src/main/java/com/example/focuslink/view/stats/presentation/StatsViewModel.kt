package com.example.focuslink.view.stats.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.core.data.SessionManager
import com.example.focuslink.view.stats.domain.StatsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class StatsViewModel() : ViewModel() {
    private val statsUseCase = StatsUseCase()

    private val _uiState = MutableStateFlow(StatsUIState())
    val uiState: StateFlow<StatsUIState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Obtener token
                val token = "Bearer ${SessionManager.getToken()}"

                // Calcular fechas de inicio y fin de la semana actual
                val (fromDate, toDate) = getCurrentWeekDates()
                Log.d("StatsViewModel", "Cargando estadísticas desde $fromDate hasta $toDate")

                // Llamar al caso de uso con las fechas
                val result = statsUseCase.getStats(token,  toDate, fromDate)

                if (result.isSuccess) {
                    val stats = result.getOrNull()
                    stats?.let { statsData ->
                        // Convertir minutos totales a horas y minutos
                        val totalHours = statsData.totalTime.toInt() / 60
                        val totalMinutes = statsData.totalTime.toInt() % 60

                        _uiState.update { currentState ->
                            currentState.copy(
                                totalCycles = statsData.focusCount,
                                totalHours = totalHours,
                                totalMinutes = totalMinutes,
                                totalInterruptions = statsData.totalInterruptions,
                                breakCount = statsData.breakCount,
                                longBreakCount = statsData.longBreakCount,
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
                Log.e("StatsViewModel", "Error al cargar estadísticas", e)
                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Error desconocido",
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Calcula las fechas de inicio y fin de la semana actual en formato ISO-8601
     * @return Par de cadenas con la fecha de inicio y fin de la semana actual
     */
    private fun getCurrentWeekDates(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        // Establecer el día al inicio de la semana (lunes)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysToSubtract = (dayOfWeek + 5) % 7 // Transformar: domingo=1, lunes=2, ... a lunes=0, martes=1, ...

        // Establecer fecha de inicio (lunes a las 00:00:00)
        calendar.add(Calendar.DAY_OF_YEAR, -daysToSubtract)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val fromDate = dateFormat.format(calendar.time)

        // Establecer fecha de fin (domingo a las 23:59:59)
        calendar.add(Calendar.DAY_OF_YEAR, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val toDate = dateFormat.format(calendar.time)

        return Pair(fromDate, toDate)
    }

    fun refreshStats() {
        loadStats()
    }
}