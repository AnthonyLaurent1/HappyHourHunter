package com.supdevinci.happyhourhunter.viewmodel

import android.app.Application
import android.location.Geocoder
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
import java.util.Locale

class WeatherCocktailViewModel(application: Application) : AndroidViewModel(application) {

    private val cocktailDao = CocktailDatabase.getDatabase(application).cocktailDao()
    private val _state = MutableStateFlow<CocktailWeatherState>(CocktailWeatherState.Loading)
    val state: StateFlow<CocktailWeatherState> = _state.asStateFlow()

    private val _citySearchError = MutableStateFlow<String?>(null)
    val citySearchError: StateFlow<String?> = _citySearchError.asStateFlow()

    private val _isCityLoading = MutableStateFlow(false)
    val isCityLoading: StateFlow<Boolean> = _isCityLoading.asStateFlow()

    private val _isInitialLoading = MutableStateFlow(true)
    val isInitialLoading: StateFlow<Boolean> = _isInitialLoading.asStateFlow()

    fun finishInitialLoading() {
        _isInitialLoading.value = false
    }
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
            finally {
                _isInitialLoading.value = false
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

    fun clearCitySearchError() {
        _citySearchError.value = null
    }


    fun fetchCocktailsForCity(city: String): Boolean {
        if (_isCityLoading.value) return false

        if (city.isBlank()) {
            _citySearchError.value = "Veuillez entrer une ville"
            return false
        }

        viewModelScope.launch {
            _isCityLoading.value = true
            _citySearchError.value = null

            try {
                val geocoder = Geocoder(getApplication(), Locale.FRENCH)
                val addresses = geocoder.getFromLocationName(city, 1)
                val address = addresses?.firstOrNull()

                if (address == null) {
                    _citySearchError.value = "Ville introuvable"
                    return@launch
                }

                val weatherData = getWeather(address.latitude, address.longitude)
                val cocktails = getCocktailsForWeather(
                    temperature = weatherData.temperature,
                    weatherMain = weatherData.weatherMain
                )

                _state.value = CocktailWeatherState.Success(
                    city = address.locality ?: city,
                    weather = weatherData.weatherDescription,
                    temperature = weatherData.temperature,
                    cocktails = cocktails
                )
            } catch (e: Exception) {
                val message = e.message.orEmpty()
                _citySearchError.value = if (message.contains("429")) {
                    "Trop de requetes. Réessayez dans quelques secondes."
                } else {
                    "Impossible de mettre a jour cette ville"
                }
            } finally {
                _isCityLoading.value = false
            }
        }

        return true
    }

}
