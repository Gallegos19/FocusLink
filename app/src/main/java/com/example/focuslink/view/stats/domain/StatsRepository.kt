package com.example.focuslink.view.stats.domain

import com.example.focuslink.view.stats.data.model.StatsDto

interface StatsRepository {
    suspend fun getStats(token: String, userId: String): Result<StatsDto>
}