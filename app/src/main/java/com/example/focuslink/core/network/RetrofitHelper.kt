package com.example.focuslink.core.network

import com.example.focuslink.view.login.data.datasource.LoginService
import com.example.focuslink.view.register.data.datasource.RegisterService
import com.example.focuslink.view.timer.data.datasource.SessionService
import com.example.focuslink.view.stats.data.datasource.StatsService
import com.example.focuslink.view.settings.data.datasource.PreferencesService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "https://api.focuslink.com/api/"

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    fun getLoginService(): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    fun getRegisterService(): RegisterService {
        return retrofit.create(RegisterService::class.java)
    }

    fun getSessionService(): SessionService {
        return retrofit.create(SessionService::class.java)
    }

    fun getStatsService(): StatsService {
        return retrofit.create(StatsService::class.java)
    }

    fun getPreferencesService(): PreferencesService {
        return retrofit.create(PreferencesService::class.java)
    }
}