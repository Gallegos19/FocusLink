package com.example.focuslink.view.stats.data.repository

import com.example.focuslink.core.network.RetrofitHelper
import com.example.focuslink.view.stats.data.datasource.StatsService
import com.example.focuslink.view.stats.data.model.StatsDto
import com.example.focuslink.view.stats.domain.StatsRepository

class StatsRepositoryImpl(
    private val statsService: StatsService = RetrofitHelper.getStatsService()
) : StatsRepository {

    override suspend fun getStats(token: String, userId: String): Result<StatsDto> {
        return try {
            val response = statsService.getStats("Bearer $token", userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}