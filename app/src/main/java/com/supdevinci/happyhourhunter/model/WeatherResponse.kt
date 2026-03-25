package com.supdevinci.happyhourhunter.model

data class OpenWeatherResponse(
    val current: OpenWeatherCurrent
)

data class OpenWeatherCurrent(
    val temperature_2m: Double,
    val weather_code: Int
)

data class WeatherUi(
    val temperature: Double,
    val weatherMain: String,
    val weatherDescription: String
)
