package com.example.focuslink.view.timer.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.focuslink.core.data.local.AppContainer

class TimerViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            // Obtenemos el AppContainer que contiene los repositorios
            val appContainer = AppContainer(context)

            // Creamos el ViewModel con el repositorio de preferencias
            return TimerViewModel(
                offlinePreferencesRepository = appContainer.preferencesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}