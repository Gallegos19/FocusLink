package com.example.focuslink.view.timer.presentation.components

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Vibrator
import android.util.Log

/**
 * Versión simplificada y robusta para reproducir sonidos de alarma
 */
class TimerSoundPlayer(context: Context) {

    // Guardar el contexto de la aplicación para evitar memory leaks
    private val appContext = context.applicationContext
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private val tag = "AlarmDebug"

    init {
        Log.d(tag, "TimerSoundPlayer inicializado con contexto: $appContext")
    }

    /**
     * Reproduce el sonido de alarma por defecto
     */
    fun playAlarmSound(vibrate: Boolean = true) {
        Log.d(tag, "Intentando reproducir alarma")

        if (isPlaying) {
            Log.d(tag, "Ya está sonando una alarma, no se inicia otra")
            return
        }

        try {
            // Detener cualquier reproducción anterior
            stopAlarmSound()

            // Usar el sonido de alarma por defecto
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            Log.d(tag, "URI de alarma: $notification")

            // Crear un MediaPlayer nuevo
            try {
                Log.d(tag, "Creando MediaPlayer...")
                mediaPlayer = MediaPlayer.create(appContext, notification)

                if (mediaPlayer != null) {
                    mediaPlayer?.isLooping = true
                    mediaPlayer?.start()
                    isPlaying = true
                    Log.d(tag, "¡MediaPlayer iniciado correctamente!")
                } else {
                    Log.e(tag, "Error: MediaPlayer es null después de create()")
                }
            } catch (e: Exception) {
                Log.e(tag, "Error al crear MediaPlayer: ${e.message}")

                try {
                    // Plan B: Usar RingtoneManager directamente
                    Log.d(tag, "Intentando con RingtoneManager...")
                    val ringtone = RingtoneManager.getRingtone(appContext, notification)
                    ringtone.play()
                    Log.d(tag, "Reproduciendo con RingtoneManager")
                } catch (e2: Exception) {
                    Log.e(tag, "Error con RingtoneManager: ${e2.message}")
                }
            }

            // Vibrar el dispositivo
            if (vibrate) {
                try {
                    Log.d(tag, "Iniciando vibración")
                    val vibrator = appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

                    vibrator?.let {
                        val pattern = longArrayOf(0, 500, 1000)
                        // Usar método compatible
                        @Suppress("DEPRECATION")
                        it.vibrate(pattern, 0) // Repetir indefinidamente (índice 0)
                        Log.d(tag, "Vibración iniciada")
                    } ?: Log.e(tag, "Vibrador es null")
                } catch (e: Exception) {
                    Log.e(tag, "Error al vibrar: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error general en playAlarmSound: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Detiene la reproducción de sonido y vibración
     */
    fun stopAlarmSound() {
        Log.d(tag, "Deteniendo alarma")

        try {
            // Detener MediaPlayer
            if (mediaPlayer != null) {
                try {
                    if (mediaPlayer?.isPlaying == true) {
                        mediaPlayer?.stop()
                    }
                    mediaPlayer?.release()
                    Log.d(tag, "MediaPlayer detenido y liberado")
                } catch (e: Exception) {
                    Log.e(tag, "Error al detener MediaPlayer: ${e.message}")
                }
            }

            mediaPlayer = null
            isPlaying = false

            // Detener vibración
            try {
                val vibrator = appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.cancel()
                Log.d(tag, "Vibración cancelada")
            } catch (e: Exception) {
                Log.e(tag, "Error al detener vibración: ${e.message}")
            }
        } catch (e: Exception) {
            Log.e(tag, "Error al detener alarma: ${e.message}")
        }
    }

    // Para eliminar referencias y evitar memory leaks
    fun release() {
        stopAlarmSound()
        mediaPlayer = null
    }
}