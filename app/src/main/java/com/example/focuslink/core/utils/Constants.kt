package com.example.focuslink.core.utils

object Constants {
    const val TOKEN_KEY = "auth_token"
    const val USER_PREFERENCES = "user_preferences"

    // Timer defaults
    const val DEFAULT_FOCUS_TIME_MINUTES = 25
    const val DEFAULT_SHORT_BREAK_MINUTES = 5
    const val DEFAULT_LONG_BREAK_MINUTES = 15
    const val DEFAULT_CYCLES_BEFORE_LONG_BREAK = 4


    // API endpoints
    const val LOGIN_ENDPOINT = "auth/login"
    const val REGISTER_ENDPOINT = "auth/register"
    const val SESSIONS_ENDPOINT = "sessions"
    const val STATS_ENDPOINT = "stats"
    const val NOTIFICATIONS_ENDPOINT = "notifications"
    const val PREFERENCES_ENDPOINT = "preferences"

    // Notification IDs
    const val TIMER_NOTIFICATION_ID = 1001
    const val FOCUS_SERVICE_NOTIFICATION_ID = 1002
}