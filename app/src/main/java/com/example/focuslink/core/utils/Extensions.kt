package com.example.focuslink.core.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Date and time formatting extensions
 */
fun Date.formatToString(pattern: String = "dd/MM/yyyy"): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this)
}

fun Long.formatTimeDuration(): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

/**
 * Vibration helper extension
 */
fun Context.vibrate(duration: Long = 500) {
    try {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    } catch (e: Exception) {
        // Handle any exceptions
    }
}

/**
 * String validation extensions
 */
fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    return this.matches(emailRegex.toRegex())
}

fun String.isValidPassword(): Boolean {
    return this.length >= 6
}

/**
 * Conversion extensions
 */
fun Int.minutesToMillis(): Long {
    return TimeUnit.MINUTES.toMillis(this.toLong())
}

fun Long.millisToMinutes(): Int {
    return TimeUnit.MILLISECONDS.toMinutes(this).toInt()
}