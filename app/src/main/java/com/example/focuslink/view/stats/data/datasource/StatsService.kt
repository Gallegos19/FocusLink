package com.example.focuslink.view.stats.data.datasource

import com.example.focuslink.core.utils.Constants
import com.example.focuslink.view.stats.data.model.StatsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StatsService {
    @GET("stats/me")
    suspend fun getStats(
        @Header("Authorization") token: String,
        @Query("from") fromDate: String,
        @Query("to") toDate: String
    ): Response<StatsDto>
}