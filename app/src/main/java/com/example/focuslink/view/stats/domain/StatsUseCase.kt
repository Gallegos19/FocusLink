package com.example.focuslink.view.stats.domain

import android.content.Context
import android.content.SharedPreferences
import com.example.focuslink.core.utils.Constants
import com.example.focuslink.view.stats.data.model.DailyStat
import com.example.focuslink.view.stats.data.model.StatsDto
import com.example.focuslink.view.stats.data.repository.StatsRepositoryImpl

class StatsUseCase(
    private val statsRepository: StatsRepository = StatsRepositoryImpl(),
    private val context: Context? = null
) {

    suspend fun execute(): Result<StatsDto> {
        // Datos mock para la UI
        val mockDailyStats = listOf(
            DailyStat("1 de abril, 2025", "2h 15m", 5),
            DailyStat("31 de marzo, 2025", "1h 45m", 4),
            DailyStat("30 de marzo, 2025", "3h 30m", 7),
            DailyStat("29 de marzo, 2025", "2h 00m", 4),
            DailyStat("28 de marzo, 2025", "1h 15m", 3)
        )

        val mockStats = StatsDto(
            userId = "mock-user-id",
            totalCycles = 23,
            totalFocusTime = 645, // 10h 45m en minutos
            dailyStats = mockDailyStats
        )

        return Result.success(mockStats)

        // En una implementación real, implementaríamos algo como:
        /*
        try {
            // Obtener token y userId de SharedPreferences
            val prefs: SharedPreferences = context?.getSharedPreferences(
                Constants.USER_PREFERENCES,
                Context.MODE_PRIVATE
            ) ?: return Result.failure(IllegalStateException("Context is null"))

            val token = prefs.getString(Constants.TOKEN_KEY, "") ?: ""
            val userId = prefs.getString("user_id", "") ?: ""

            if (token.isEmpty() || userId.isEmpty()) {
                return Result.failure(IllegalStateException("No se encontraron credenciales"))
            }

            return statsRepository.getStats(token, userId)
        } catch (e: Exception) {
            return Result.failure(e)
        }
        */
    }
}