package com.example.focuslink.view.timer.presentation

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslink.core.data.GlobalStorage
import com.example.focuslink.core.data.SessionManager
import com.example.focuslink.core.data.local.preferences.repository.OfflinePreferencesRepository
import com.example.focuslink.core.services.TimerForegroundService
import com.example.focuslink.view.timer.data.model.SessionRequest
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
import java.util.Date

class TimerViewModel(
    private val offlinePreferencesRepository: OfflinePreferencesRepository? = null
) : ViewModel() {
    private val addSessionUseCase = AddsSessionUseCase()
    private val tag = "TimerDebug"

    private val _uiState = MutableStateFlow(TimerUIState())
    val uiState: StateFlow<TimerUIState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    // Variable para el MediaPlayer
    private var mediaPlayer: MediaPlayer? = null

    // Valores por defecto (se usarán si no podemos obtener los valores guardados)
    private var focusTimeMinutes = 25
    private var breakTimeMinutes = 5
    private var longBreakTimeMinutes = 10

    // Tiempos calculados en milisegundos
    private var focusTimeMillis = TimeUnit.MINUTES.toMillis(focusTimeMinutes.toLong())
    private var breakTimeMillis = TimeUnit.MINUTES.toMillis(breakTimeMinutes.toLong())
    private var longBreakTimeMillis = TimeUnit.MINUTES.toMillis(longBreakTimeMinutes.toLong())

    // Inicializamos con el tiempo de enfoque
    private var currentTimeLeftMillis = focusTimeMillis
    private var initialTimeMillis = focusTimeMillis

    // Tracking para las sesiones
    private var sessionStartTime: Date? = null
    private var wasInterrupted = false
    private val interruptionReasons = mutableListOf<String>()

    init {
        viewModelScope.launch {
            // Cargar preferencias del usuario
            loadUserPreferences()

            // Actualizar el formato de tiempo mostrado inicialmente
            _uiState.update {
                it.copy(timeLeftFormatted = formatTime(currentTimeLeftMillis))
            }
            Log.d(tag, "ViewModel inicializado con focusTime: $focusTimeMinutes min, breakTime: $breakTimeMinutes min")
        }
    }

    private suspend fun loadUserPreferences() {
        try {
            val userId = SessionManager.getUserId() ?: GlobalStorage.getUserId()

            if (userId != null && offlinePreferencesRepository != null) {
                val prefs = offlinePreferencesRepository.getByUser(userId)

                if (prefs != null) {
                    Log.d(tag, "Preferencias cargadas: ${prefs.focusTime} min focus, ${prefs.shortBreak} min break, ${prefs.longBreak} min long break")

                    // Actualizar valores con los de la base de datos
                    focusTimeMinutes = prefs.focusTime
                    breakTimeMinutes = prefs.shortBreak
                    longBreakTimeMinutes = prefs.longBreak

                    // Convertir a milisegundos
                    focusTimeMillis = TimeUnit.MINUTES.toMillis(focusTimeMinutes.toLong())
                    breakTimeMillis = TimeUnit.MINUTES.toMillis(breakTimeMinutes.toLong())
                    longBreakTimeMillis = TimeUnit.MINUTES.toMillis(longBreakTimeMinutes.toLong())

                    // Reiniciar el timer con los nuevos valores
                    currentTimeLeftMillis = focusTimeMillis
                    initialTimeMillis = focusTimeMillis

                    // Actualizar UI
                    _uiState.update {
                        it.copy(timeLeftFormatted = formatTime(currentTimeLeftMillis))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error cargando preferencias: ${e.message}")
        }
    }

    fun initSoundPlayer(context: Context) {
        // Este método ya no hace nada, se mantiene por compatibilidad
        Log.d(tag, "initSoundPlayer llamado, pero ya no se necesita")
    }

    fun startTimer() {
        if (timerJob == null) {
            // Registrar tiempo de inicio de sesión
            sessionStartTime = Date()
            wasInterrupted = false
            interruptionReasons.clear()

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
                    // Cuando termina el temporizador
                    timerCompleted()
                }
            }
        }
    }

    fun pauseTimer() {
        // Marcar como interrumpido si estaba corriendo
        if (_uiState.value.isRunning) {
            wasInterrupted = true
            interruptionReasons.add("User Paused")
        }

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
        // Registrar la sesión interrumpida si estaba corriendo
        if (_uiState.value.isRunning) {
            wasInterrupted = true
            interruptionReasons.add("User Stopped")
            registerSessionIfNeeded()
        }

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
        // Registrar la sesión completada
        registerSessionIfNeeded()

        // No reproducimos aquí el sonido, lo haremos desde la UI que tiene acceso al contexto
        _uiState.update { currentState ->
            Log.d(tag, "Estado al completarse: isRunning=${currentState.isRunning}, isBreakTime=${currentState.isBreakTime}, ciclo=${currentState.currentCycle}")

            // Cambiamos entre tiempo de enfoque y descanso
            val newIsBreakTime = !currentState.isBreakTime

            // Incrementamos el ciclo solo cuando pasamos de descanso a enfoque
            val newCycle = if (newIsBreakTime) {
                currentState.currentCycle
            } else {
                currentState.currentCycle + 1
            }

            // Determinar el siguiente tiempo basado en si es tiempo de enfoque o descanso
            val nextTimerDuration = if (newIsBreakTime) {
                // Si ahora toca tiempo de descanso
                breakTimeMillis
            } else {
                // Si ahora toca tiempo de enfoque
                focusTimeMillis
            }

            Log.d(tag, "Próximo timer: ${if (newIsBreakTime) "DESCANSO" else "ENFOQUE"} de ${TimeUnit.MILLISECONDS.toMinutes(nextTimerDuration)} minutos")

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
            // IMPORTANTE: Reasignamos correctamente los tiempos para el siguiente ciclo
            if (_uiState.value.isBreakTime) {
                currentTimeLeftMillis = breakTimeMillis
                initialTimeMillis = breakTimeMillis
            } else {
                currentTimeLeftMillis = focusTimeMillis
                initialTimeMillis = focusTimeMillis
            }

            Log.d(tag, "Actualizando para el siguiente ciclo. Tiempo inicial: ${formatTime(currentTimeLeftMillis)}")

            _uiState.update { currentState ->
                currentState.copy(
                    progress = 1f,
                    timeLeftFormatted = formatTime(currentTimeLeftMillis)
                )
            }

            // Reiniciar tracking para la próxima sesión
            sessionStartTime = null
        }

        timerJob = null
        Log.d(tag, "timerCompleted() finalizado")
    }

    private fun registerSessionIfNeeded() {
        viewModelScope.launch {
            try {
                // Verificar si hay un inicio de sesión registrado
                val startTime = sessionStartTime
                if (startTime != null) {
                    val endTime = Date()
                    val type = if (_uiState.value.isBreakTime) "break" else "focus"

                    Log.d(tag, "Registrando sesión - Tipo: $type, Inicio: $startTime, Fin: $endTime")

                    // Crear objeto de sesión con nombre de propiedad corregido
                    val sessionRequest = SessionRequest(
                        startTimeDate = startTime,
                        endTimeDate = endTime,
                        type = type,
                        wasInterrupted = wasInterrupted,  // Nombre corregido (era wasInterrumped)
                        interruptedBy = interruptionReasons
                    )

                    // Token de autenticación
                    val token = "${SessionManager.getToken()}"

                    // Llamar al caso de uso
                    val result = addSessionUseCase.addSession(token, sessionRequest)

                    if (result.isSuccess) {
                        Log.d(tag, "Sesión registrada correctamente: ${result.getOrNull()}")
                    } else {
                        Log.e(tag, "Error al registrar sesión: ${result.exceptionOrNull()?.message}")
                    }

                    // Preparar para la siguiente sesión
                    sessionStartTime = Date() // Iniciar la siguiente sesión inmediatamente
                    wasInterrupted = false
                    interruptionReasons.clear()
                }
            } catch (e: Exception) {
                Log.e(tag, "Error registrando sesión: ${e.message}")
                e.printStackTrace() // Added stack trace for better debugging
            }
        }
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