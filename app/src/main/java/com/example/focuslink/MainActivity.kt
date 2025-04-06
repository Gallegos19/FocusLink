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
import com.example.focuslink.core.data.GlobalStorage
import com.example.focuslink.core.data.SessionManager
import com.example.focuslink.core.navigation.NavigationWrapper
import com.example.focuslink.core.theme.ThemeManager
import com.example.focuslink.ui.theme.FocusLinkTheme
import com.example.focuslink.view.settings.presentation.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalStorage.init(this)
        SessionManager.init(this)

        setContent {
            val isDarkTheme by ThemeManager.isDarkTheme.collectAsState()

            FocusLinkTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationWrapper(
                        ctx = this,
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }
    }
}