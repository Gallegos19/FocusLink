package com.example.focuslink

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.example.focuslink.core.data.SessionManager
import com.example.focuslink.core.theme.ThemeManager
import com.example.focuslink.utils.save_token.data.model.TokenDTO
import com.example.focuslink.utils.save_token.domain.SaveTokenUseCase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApp : Application() {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "notification_fcm"
    }

    override fun onCreate() {
        val saveToken = SaveTokenUseCase()
        super.onCreate()
        ThemeManager.init(this)

        println("App creada")

        // Crear canal de notificaciones - ¡IMPORTANTE!
        createNotificationChannel()

        Firebase.messaging.token.addOnCompleteListener {
            if(!it.isSuccessful){
                Log.d("dbug", "Token no fue generado")
                println("El token no fue generado correctamente")
                return@addOnCompleteListener
            }
            val token = it.result
            SessionManager.saveFCMToken(token)
            val userId = SessionManager.getUserId()
            if (userId > 0) {
                val bodytoken:TokenDTO = TokenDTO(SessionManager.getUserId(),token)
                CoroutineScope(Dispatchers.IO).launch {
                    saveToken.saveToken(bodytoken)
                }
            }


            println("El valor del token es")
            println(token)
            Log.d("dbug", token)
        }
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Notificacion de fcm",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Esta notificacion va ser recibida desde fcm"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}