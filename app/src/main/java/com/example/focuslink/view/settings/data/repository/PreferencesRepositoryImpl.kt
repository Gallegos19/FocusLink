package com.example.focuslink.view.settings.data.repository

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.example.focuslink.core.network.RetrofitHelper
import com.example.focuslink.view.settings.data.datasource.PreferencesService
import com.example.focuslink.view.settings.data.datasource.PreferencesServices
import com.example.focuslink.view.settings.data.model.PreferencesDto
import com.example.focuslink.view.settings.data.model.PreferencesRequest
import com.example.focuslink.view.settings.data.model.PreferencesResponse
import com.example.focuslink.view.settings.data.model.UserResponse
import com.example.focuslink.view.settings.domain.PreferencesRepository

class PreferencesRepositoryImpl{
    private val context: Context? = null
    private val preferencesService = RetrofitHelper.getPreferencesService()

    suspend fun getPreferences(token:String): Result<PreferencesResponse> {
        return try {
            val response = preferencesService.getPreferences(token)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Respuesta vacía por parte del servidor"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido del servidor"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun savePreferences(token: String, preference: PreferencesRequest): Result<PreferencesResponse> {
        return try {
            val response = preferencesService.savePreferences(token, preference)
            if(response.isSuccessful){
                response.body()?.let {
                    Result.success(it)
                }?: Result.failure(Exception("Respuesta vacia por parte del servidor"))
            }else{
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(token: String): Result<UserResponse> {
        return try {
            val response = preferencesService.getUserById(token)
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("PreferencesRepository", "User response: $body")
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Respuesta vacía por parte del servidor"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido del servidor"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("PreferencesRepository", "Error getting user", e)
            Result.failure(e)
        }
    }
}