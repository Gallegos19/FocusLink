package com.example.focuslink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.focuslink.core.navigation.NavigationWrapper
import com.example.focuslink.ui.theme.FocusLinkTheme
import com.example.focuslink.view.settings.presentation.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Obtener preferencias para determinar el tema
            val settingsViewModel: SettingsViewModel = viewModel()
            val uiState by settingsViewModel.uiState.collectAsState()

            FocusLinkTheme(darkTheme = uiState.darkModeEnabled) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationWrapper(ctx = this)
                }
            }
        }
    }
}