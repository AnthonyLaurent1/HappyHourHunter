package com.supdevinci.happyhourhunter.data

import com.supdevinci.happyhourhunter.service.CocktailApiService
import com.supdevinci.happyhourhunter.service.OpenWeatherService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val COCKTAIL_BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"
    private const val WEATHER_BASE_URL = "https://api.open-meteo.com/v1/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    val cocktailApi: CocktailApiService by lazy {
        Retrofit.Builder()
            .baseUrl(COCKTAIL_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CocktailApiService::class.java)
    }

    val weatherApi: OpenWeatherService by lazy {
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherService::class.java)
    }
}
