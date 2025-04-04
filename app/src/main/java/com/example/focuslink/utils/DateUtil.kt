package com.example.focuslink.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Utilidad para manejo de fechas en la aplicación
 */
object DateUtil {
    /**
     * Convierte un objeto Date a formato ISO 8601 en UTC
     * Formato: "2025-04-04T07:41:12.484Z"
     */
    fun toISOString(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(date)
    }

    /**
     * Alternativa usando el formato ISO que no incluye milisegundos
     * Formato: "2025-04-04T07:41:12Z"
     */
    fun toISOStringSimple(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(date)
    }
}