package com.supdevinci.happyhourhunter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.happyhourhunter.data.RetrofitInstance
import com.supdevinci.happyhourhunter.data.choiceCategoryCocktail
import com.supdevinci.happyhourhunter.data.local.CocktailDatabase
import com.supdevinci.happyhourhunter.data.local.toDrink
import com.supdevinci.happyhourhunter.data.local.toEntity
import com.supdevinci.happyhourhunter.data.weatherCodeToDescription
import com.supdevinci.happyhourhunter.data.weatherCodeToMain
import com.supdevinci.happyhourhunter.model.Drink
import com.supdevinci.happyhourhunter.model.WeatherUi
import com.supdevinci.happyhourhunter.viewmodel.states.CocktailWeatherState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherCocktailViewModel(application: Application) : AndroidViewModel(application) {

    private val cocktailDao = CocktailDatabase.getDatabase(application).cocktailDao()
    private val _state = MutableStateFlow<CocktailWeatherState>(CocktailWeatherState.Loading)
    val state: StateFlow<CocktailWeatherState> = _state.asStateFlow()

    fun fetchCocktailsForLocation(lat: Double, lon: Double, city: String) {

        viewModelScope.launch {
            _state.value = CocktailWeatherState.Loading

            try {
                val weatherData = getWeather(lat, lon)
                val cocktails = getCocktailsForWeather(
                    weatherData.temperature,
                    weatherData.weatherMain
                )

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

    private suspend fun getCocktailsForWeather(
        temperature: Double,
        weatherMain: String
    ): List<Drink> {
        val category = choiceCategoryCocktail(temperature, weatherMain)

        val basicCocktails = RetrofitInstance.cocktailApi
            .getCocktailsByCategory(category)
            .drinks
            .shuffled()
            .take(4)

        val fullCocktails = basicCocktails.mapNotNull { basicDrink ->
            val existingCocktail = cocktailDao.getByApiId(basicDrink.idDrink)

            if (existingCocktail != null) {
                existingCocktail.toDrink()
            } else {
                val detailResponse = RetrofitInstance.cocktailApi.getCocktailDetail(basicDrink.idDrink)
                val fullDrink = detailResponse.drinks?.firstOrNull()

                if (fullDrink != null) {
                    cocktailDao.insert(fullDrink.toEntity())
                }

                fullDrink
            }
        }

        return fullCocktails
    }

    private suspend fun getWeather(lat: Double, lon: Double): WeatherUi {
        val weatherResponse = RetrofitInstance.weatherApi.getCurrentWeather(
            latitude = lat,
            longitude = lon
        )

        val temperature = weatherResponse.current.temperature_2m
        val weatherCode = weatherResponse.current.weather_code

        return WeatherUi(
            temperature = temperature,
            weatherMain = weatherCodeToMain(weatherCode),
            weatherDescription = weatherCodeToDescription(weatherCode)
        )
    }
}
