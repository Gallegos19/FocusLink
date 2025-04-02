package com.example.focuslink.view.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.core.utils.Constants
import com.example.focuslink.view.timer.domain.StartTimerUseCase
import com.example.focuslink.view.timer.domain.StopTimerUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TimerViewModel(
    private val startTimerUseCase: StartTimerUseCase = StartTimerUseCase(),
    private val stopTimerUseCase: StopTimerUseCase = StopTimerUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUIState())
    val uiState: StateFlow<TimerUIState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    // Default focus time is 25 minutes
    private val focusTimeMillis = TimeUnit.MINUTES.toMillis(Constants.DEFAULT_FOCUS_TIME_MINUTES.toLong())
    // Default break time is 5 minutes
    private val breakTimeMillis = TimeUnit.MINUTES.toMillis(Constants.DEFAULT_SHORT_BREAK_MINUTES.toLong())

    private var currentTimeLeftMillis = focusTimeMillis
    private var initialTimeMillis = focusTimeMillis

    // Simplificado para demo UI
    fun startTimer() {
        if (timerJob == null) {
            timerJob = viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        isRunning = true,
                        isPaused = false
                    )
                }

                // Simulación de timer para la UI
                while (currentTimeLeftMillis > 0 && _uiState.value.isRunning) {
                    delay(100) // Actualizar cada 100ms para progreso suave
                    currentTimeLeftMillis -= 100
                    updateTimerState()
                }

                if (currentTimeLeftMillis <= 0) {
                    timerCompleted()
                }
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
        _uiState.update {
            it.copy(
                isRunning = false,
                isPaused = true
            )
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null

        resetTimer()
    }

    private fun resetTimer() {
        currentTimeLeftMillis = if (_uiState.value.isBreakTime) {
            breakTimeMillis
        } else {
            focusTimeMillis
        }

        initialTimeMillis = currentTimeLeftMillis

        _uiState.update {
            it.copy(
                isRunning = false,
                isPaused = false,
                progress = 1f,
                timeLeftFormatted = formatTime(currentTimeLeftMillis)
            )
        }
    }

    private fun timerCompleted() {
        _uiState.update { currentState ->
            val newIsBreakTime = !currentState.isBreakTime
            val newCycle = if (newIsBreakTime) {
                currentState.currentCycle
            } else {
                currentState.currentCycle + 1
            }

            val nextTimerDuration = if (newIsBreakTime) {
                breakTimeMillis
            } else {
                focusTimeMillis
            }

            currentTimeLeftMillis = nextTimerDuration
            initialTimeMillis = nextTimerDuration

            currentState.copy(
                isRunning = false,
                isPaused = false,
                isBreakTime = newIsBreakTime,
                currentCycle = newCycle,
                progress = 1f,
                timeLeftFormatted = formatTime(nextTimerDuration)
            )
        }

        timerJob = null
    }

    private fun updateTimerState() {
        _uiState.update {
            it.copy(
                progress = currentTimeLeftMillis.toFloat() / initialTimeMillis.toFloat(),
                timeLeftFormatted = formatTime(currentTimeLeftMillis)
            )
        }
    }

    private fun formatTime(timeMillis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}