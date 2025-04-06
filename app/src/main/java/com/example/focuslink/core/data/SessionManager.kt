package com.example.focuslink.core.data

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.messaging.FirebaseMessaging
import android.util.Log

object SessionManager {

    private const val PREFS_NAME = "app_prefs"
    private const val TOKEN_KEY = "TOKEN"
    private const val USER_ID_KEY = "USER_ID"
    private const val FCM_TOKEN_KEY = "FCM_TOKEN"

    private lateinit var applicationContext: Context

    // Usando inicialización perezosa para asegurar que sharedPreferences siempre esté inicializado antes de usarse
    private val sharedPreferences: SharedPreferences by lazy {
        if (::applicationContext.isInitialized) {
            applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        } else {
            throw IllegalStateException("SessionManager no ha sido inicializado. Llama a init(context) antes de usar cualquier método.")
        }
    }

    fun init(context: Context) {
        applicationContext = context.applicationContext // Usar applicationContext para prevenir memory leaks
    }

    // Guardar token de sesión
    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    // Obtener token de sesión
    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    // Guardar ID del usuario
    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString(USER_ID_KEY, userId).apply()
    }

    // Obtener ID del usuario
    fun getUserId(): String? {
        return sharedPreferences.getString(USER_ID_KEY, null)
    }

    // Borrar sesión completa (Token + ID + FCM)
    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }

    // Guardar token de FCM
    fun saveFCMToken(fcmToken: String) {
        sharedPreferences.edit().putString(FCM_TOKEN_KEY, fcmToken).apply()
    }

    // Obtener token de FCM
    fun getFCMToken(): String? {
        return sharedPreferences.getString(FCM_TOKEN_KEY, null)
    }

    // Verifica si el token de FCM ha cambiado y lo envía al backend si es necesario
    fun updateFCMTokenIfNeeded(sendTokenToBackend: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("SessionManager", "Error al obtener el token FCM", task.exception)
                return@addOnCompleteListener
            }

            val newToken = task.result
            val savedToken = getFCMToken()

            if (newToken != null && newToken != savedToken) {
                saveFCMToken(newToken)
                sendTokenToBackend(newToken) // Envía el nuevo token al backend
            }
        }
    }
}