package com.example.focuslink.utils.save_token.data.repository

import com.example.focuslink.core.network.RetrofitHelper
import com.example.focuslink.utils.save_token.data.datasource.SaveTokenService
import com.example.focuslink.utils.save_token.data.model.TokenDTO

class SaveTokenRepository {

    private val service = RetrofitHelper.getRetrofitToken()

    suspend fun saveToken(bodyToken: TokenDTO): Result<String> {
        return try {
            val response = service.saveToken(bodyToken)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacia por parte del servidor"))
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}