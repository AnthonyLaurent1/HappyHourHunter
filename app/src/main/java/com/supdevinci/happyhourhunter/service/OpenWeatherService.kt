package com.supdevinci.happyhourhunter.service

import com.supdevinci.happyhourhunter.model.OpenWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {
    @GET("forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,weather_code",
    ): OpenWeatherResponse
}