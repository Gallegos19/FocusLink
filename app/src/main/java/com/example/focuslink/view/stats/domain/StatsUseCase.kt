package com.example.focuslink.view.stats.domain

import android.content.Context
import android.content.SharedPreferences
import com.example.focuslink.core.utils.Constants
import com.example.focuslink.view.stats.data.model.StatsDto
import com.example.focuslink.view.stats.data.repository.StatsRepositoryImpl

class StatsUseCase() {
    private val statsRepository = StatsRepositoryImpl()
    private val context: Context? = null

    suspend fun getStats(token:String, toDate:String,fromDate:String): Result<StatsDto> {
        val result = statsRepository.getStats(token, fromDate, toDate)
        return result
    }

}