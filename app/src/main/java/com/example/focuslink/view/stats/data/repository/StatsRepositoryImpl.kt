package com.example.focuslink.view.stats.data.repository

import com.example.focuslink.core.network.RetrofitHelper
import com.example.focuslink.view.stats.data.datasource.StatsService
import com.example.focuslink.view.stats.data.model.StatsDto
import com.example.focuslink.view.stats.domain.StatsRepository

class StatsRepositoryImpl(){
    private val statsService: StatsService = RetrofitHelper.getStatsService()

    suspend fun getStats(token: String, fromDate: String, toDate:String): Result<StatsDto> {
        return try {
            val response = statsService.getStats(token, fromDate, toDate)
            if (response.isSuccessful){
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacia por parte del servidor"))
            }else{
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}