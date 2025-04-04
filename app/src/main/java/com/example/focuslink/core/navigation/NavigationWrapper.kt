package com.example.focuslink.core.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.focuslink.view.login.presentation.LoginScreen
import com.example.focuslink.view.login.presentation.LoginViewModel
import com.example.focuslink.view.register.presentation.RegisterScreen
import com.example.focuslink.view.register.presentation.RegisterViewModel
import com.example.focuslink.view.timer.presentation.TimerScreen
import com.example.focuslink.view.timer.presentation.TimerViewModel
import com.example.focuslink.view.stats.presentation.StatsScreen
import com.example.focuslink.view.stats.presentation.StatsViewModel
import com.example.focuslink.view.notifications.presentation.NotificationsScreen
import com.example.focuslink.view.notifications.presentation.NotificationsViewModel
import com.example.focuslink.view.settings.presentation.SettingsScreen
import com.example.focuslink.view.settings.presentation.SettingsViewModel
import com.example.focuslink.view.settings.presentation.SettingsViewModelFactory

@Composable
fun NavigationWrapper(
    modifier: Modifier = Modifier,
    ctx: Context,
    isDarkTheme: Boolean = false
) {
    val navController = rememberNavController()

    val context = LocalContext.current

    // Crear ViewModel usando nuestro Factory personalizado
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context)
    )

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                loginViewModel = LoginViewModel(),
                navigateToHome = { navController.navigate(Screen.Timer.route) },
                navigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                registerViewModel = RegisterViewModel(),
                navigateToLogin = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Timer.route) {
            TimerScreen(
                navigateToStats = { navController.navigate(Screen.Stats.route) },
                navigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                navigateToSettings = { navController.navigate(Screen.Settings.route) },
                isDarkTheme = isDarkTheme
            )
        }

        composable(Screen.Stats.route) {
            StatsScreen(
                statsViewModel = StatsViewModel(),
                navigateToTimer = { navController.navigate(Screen.Timer.route) },
                navigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                navigateToSettings = { navController.navigate(Screen.Settings.route) },
                isDarkTheme = isDarkTheme
            )
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen(
                notificationsViewModel = NotificationsViewModel(),
                navigateToTimer = { navController.navigate(Screen.Timer.route) },
                navigateToStats = { navController.navigate(Screen.Stats.route) },
                navigateToSettings = { navController.navigate(Screen.Settings.route) },
                isDarkTheme = isDarkTheme
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                navigateToTimer = { navController.navigate(Screen.Timer.route) },
                navigateToStats = { navController.navigate(Screen.Stats.route) },
                navigateToLogin = {navController.navigate(Screen.Login.route)},
                navigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                isDarkTheme = isDarkTheme
            )
        }
    }
}