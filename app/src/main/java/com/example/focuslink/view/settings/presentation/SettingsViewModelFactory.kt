package com.example.focuslink.view.settings.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.focuslink.core.data.local.AppContainer
import com.example.focuslink.view.settings.domain.PreferencesUseCase

/**
 * Factory para crear instancias de SettingsViewModel con las dependencias necesarias
 */
class SettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            // Obtenemos el AppContainer que contiene los repositorios
            val appContainer = AppContainer(context)

            // Creamos el ViewModel con el repositorio de preferencias
            return SettingsViewModel(
                preferencesUseCase = PreferencesUseCase(),
                offlinePreferencesRepository = appContainer.preferencesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}