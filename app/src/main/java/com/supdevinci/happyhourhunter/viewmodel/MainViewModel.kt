package com.supdevinci.happyhourhunter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.happyhourhunter.data.RetrofitInstance
import com.supdevinci.happyhourhunter.data.choiceCategoryCocktail
import com.supdevinci.happyhourhunter.data.weatherCodeToDescription
import com.supdevinci.happyhourhunter.data.weatherCodeToMain
import com.supdevinci.happyhourhunter.model.Drink
import com.supdevinci.happyhourhunter.model.WeatherUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private var lastLat: Double? = null
    private var lastLon: Double? = null

    private val _state = MutableStateFlow<CocktailWeatherState>(CocktailWeatherState.Loading)
    val state: StateFlow<CocktailWeatherState> = _state.asStateFlow()

    private val _detailState = MutableStateFlow< CocktailDetailState>(CocktailDetailState.Loading)
    val detailState: StateFlow<CocktailDetailState> = _detailState.asStateFlow()

    fun fetchCocktailsForLocation(lat: Double, lon: Double, city: String) {
        lastLat = lat
        lastLon = lon

        viewModelScope.launch {
            _state.value = CocktailWeatherState.Loading

            try {
                val weatherData = getWeather(lat, lon)
                val cocktails = getCocktailsForWeather(weatherData.temperature, weatherData.weatherMain)

                _state.value = CocktailWeatherState.Success(
                    city = city,
                    weather = weatherData.weatherDescription,
                    temperature = weatherData.temperature,
                    cocktails = cocktails
                )
            } catch (e: Exception) {
                _state.value = CocktailWeatherState.Error(
                    "Erreur reseau : ${e.message}"
                )
            }
        }
    }

    private suspend fun getCocktailsForWeather(temperature: Double, weatherMain: String): List<Drink> {
        val category = choiceCategoryCocktail(temperature, weatherMain)
        val cocktailResponse = RetrofitInstance.cocktailApi.getCocktailsByCategory(category)
        return cocktailResponse.drinks.shuffled().take(3)
    }

    private suspend fun getWeather(lat: Double, lon: Double): WeatherUi {
        val weatherResponse = RetrofitInstance.weatherApi.getCurrentWeather(
            latitude = lat,
            longitude = lon
        )

        val temperature = weatherResponse.current.temperature_2m
        val weatherCode = weatherResponse.current.weather_code
        val weatherMain = weatherCodeToMain(weatherCode)
        val weatherDescription = weatherCodeToDescription(weatherCode)

        return WeatherUi(
            temperature = temperature,
            weatherMain = weatherMain,
            weatherDescription = weatherDescription
        )
    }




    fun fetchCocktailDetail(id: String) {
        viewModelScope.launch {
            _detailState.value = CocktailDetailState.Loading

            try {
                val response = RetrofitInstance.cocktailApi.getCocktailDetail(id)
                val drink = response.drinks.firstOrNull()

                if (drink != null) {
                    _detailState.value = CocktailDetailState.Success(drink)
                } else {
                    _detailState.value = CocktailDetailState.Error("Cocktail introuvable")
                }
            } catch (e: Exception) {
                _detailState.value = CocktailDetailState.Error("Erreur reseau : ${e.message}")
            }
        }
    }
}