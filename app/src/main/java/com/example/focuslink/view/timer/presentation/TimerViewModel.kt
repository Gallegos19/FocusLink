package com.example.focuslink.view.timer.presentation

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.core.services.TimerForegroundService
import com.example.focuslink.view.timer.domain.AddsSessionUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import android.util.Log

class TimerViewModel() : ViewModel() {
    private val AddSession = AddsSessionUseCase()
    private val tag = "AlarmDebug"

    private val _uiState = MutableStateFlow(TimerUIState())
    val uiState: StateFlow<TimerUIState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    // Variable para el MediaPlayer
    private var mediaPlayer: MediaPlayer? = null

    // Tiempo reducido para pruebas (10 segundos para enfoque)
    private val focusTimeMillis = TimeUnit.SECONDS.toMillis(10)
    // Tiempo reducido para pruebas (5 segundos para descanso)
    private val breakTimeMillis = TimeUnit.SECONDS.toMillis(5)

    // Inicializamos con el tiempo de prueba
    private var currentTimeLeftMillis = focusTimeMillis
    private var initialTimeMillis = focusTimeMillis

    init {
        // Aseguramos que el formato de tiempo mostrado inicialmente sea correcto
        _uiState.update {
            it.copy(timeLeftFormatted = formatTime(currentTimeLeftMillis))
        }
        Log.d(tag, "ViewModel inicializado")
    }

    // Ya no guardaremos el contexto, cada método que lo necesite lo recibirá directamente
    fun initSoundPlayer(context: Context) {
        // Este método ya no hace nada, se mantiene por compatibilidad
        Log.d(tag, "initSoundPlayer llamado, pero ya no se necesita")
    }

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
                    // Cuando termina el temporizador, notificamos pero no reproducimos aún
                    // La reproducción se hará desde la UI pasando el contexto
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
        stopAlarmSound()
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

        // No reproducimos aquí el sonido, lo haremos desde la UI que tiene acceso al contexto
        _uiState.update { currentState ->
            Log.d(tag, "Estado al completarse: isRunning=${currentState.isRunning}, isBreakTime=${currentState.isBreakTime}, ciclo=${currentState.currentCycle}")

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

            Log.d(tag, "Forzando timeLeftFormatted=00:00 y progress=0")
            // Aseguramos que el tiempo formateado sea 00:00 para que la UI lo detecte
            currentState.copy(
                isRunning = false,
                isPaused = false,
                isBreakTime = newIsBreakTime,
                currentCycle = newCycle,
                progress = 0f,  // Forzamos a 0 para asegurar que la UI lo detecte
                timeLeftFormatted = "00:00"  // Forzamos a 00:00 para asegurar que la UI lo detecte
            )
        }

        // Delay para asegurar que la UI detecte el estado de finalización
        viewModelScope.launch {
            Log.d(tag, "Esperando un momento para que la UI detecte el fin...")
            delay(500)  // Delay más largo para dar tiempo

            // Actualizar para el siguiente ciclo
            Log.d(tag, "Actualizando para el siguiente ciclo")
            _uiState.update { currentState ->
                currentState.copy(
                    progress = 1f,
                    timeLeftFormatted = formatTime(currentTimeLeftMillis)
                )
            }
        }

        timerJob = null
        Log.d(tag, "timerCompleted() finalizado")
    }

    // Método para reproducir alarma - ahora requiere que se pase el contexto
    fun playAlarmSound(context: Context) {
        Log.d(tag, "Contexto recibido: $context")

        try {
            // Detener cualquier reproducción anterior
            stopAlarmSound(context)

            try {
                // Reproducir sonido directamente
                val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                Log.d(tag, "URI de alarma: $notification")

                mediaPlayer = MediaPlayer.create(context, notification)

                if (mediaPlayer == null) {
                    Log.e(tag, "ERROR: MediaPlayer es NULL después de create()")
                } else {
                    mediaPlayer?.isLooping = true
                    mediaPlayer?.start()
                    Log.d(tag, "¡MediaPlayer iniciado correctamente!")

                    // Vibrar el dispositivo
                    try {
                        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                        if (vibrator == null) {
                            Log.e(tag, "ERROR: No se pudo obtener el servicio Vibrator")
                        } else {
                            val pattern = longArrayOf(0, 500, 1000)
                            @Suppress("DEPRECATION")
                            vibrator.vibrate(pattern, 0)
                            Log.d(tag, "Vibración activada")
                        }
                    } catch (e: Exception) {
                        Log.e(tag, "Error al vibrar: ${e.message}")
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "Error reproduciendo sonido: ${e.message}")
                e.printStackTrace()

                // Intento alternativo con RingtoneManager
                try {
                    Log.d(tag, "Intentando método alternativo...")
                    val ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    ringtone.play()
                    Log.d(tag, "Reproducción alternativa iniciada")
                } catch (e2: Exception) {
                    Log.e(tag, "Error también con RingtoneManager: ${e2.message}")
                    e2.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error general en playAlarmSound: ${e.message}")
            e.printStackTrace()
        }
    }

    // Función para detener el sonido - ya no necesita contexto
    fun stopAlarmSound(context: Context? = null) {
        Log.d(tag, "Contexto recibido: $context")

        try {
            // Detener MediaPlayer
            if (mediaPlayer == null) {
                Log.d(tag, "MediaPlayer ya era null")
            } else {
                mediaPlayer?.apply {
                    val wasPlaying = isPlaying
                    Log.d(tag, "¿MediaPlayer estaba reproduciendo? $wasPlaying")

                    if (isPlaying) {
                        stop()
                        Log.d(tag, "MediaPlayer detenido")
                    }
                    release()
                    Log.d(tag, "MediaPlayer liberado")
                }
                mediaPlayer = null
            }

            // Detener vibración - necesitamos contexto para esto
            if (context == null) {
                Log.e(tag, "No hay contexto disponible para detener vibración")
            } else {
                try {
                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                    if (vibrator == null) {
                        Log.e(tag, "ERROR: No se pudo obtener el servicio Vibrator")
                    } else {
                        vibrator.cancel()
                        Log.d(tag, "Vibración cancelada")
                    }
                } catch (e: Exception) {
                    Log.e(tag, "Error al detener vibración: ${e.message}")
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error general al detener alarma: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun updateTimerState() {
        _uiState.update {
            it.copy(
                progress = currentTimeLeftMillis.toFloat() / initialTimeMillis.toFloat(),
                timeLeftFormatted = formatTime(currentTimeLeftMillis)
            )
        }
    }

    fun Context.startFocusNotification(timeLeft: String, progress: Float) {
        val intent = Intent(this, TimerForegroundService::class.java)
        intent.putExtra("timeLeft", timeLeft)
        intent.putExtra("progress", progress)
        ContextCompat.startForegroundService(this, intent)
    }

    private fun formatTime(timeMillis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        stopAlarmSound()
    }
}