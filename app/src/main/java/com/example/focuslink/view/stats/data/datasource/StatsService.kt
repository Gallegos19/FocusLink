package com.example.focuslink.view.stats.data.datasource

import com.example.focuslink.core.utils.Constants
import com.example.focuslink.view.stats.data.model.StatsDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StatsService {
    @GET(Constants.STATS_ENDPOINT)
    suspend fun getStats(
        @Header("Authorization") token: String,
        @Query("userId") userId: String
    ): StatsDto
}