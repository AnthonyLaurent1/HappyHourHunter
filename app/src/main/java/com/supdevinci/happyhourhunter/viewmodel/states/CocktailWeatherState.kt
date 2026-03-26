package com.supdevinci.happyhourhunter.viewmodel.states

import com.supdevinci.happyhourhunter.model.Drink

sealed interface CocktailWeatherState {
    data object Loading : CocktailWeatherState

    data class Success(
        val city: String,
        val weather: String,
        val temperature: Double,
        val cocktails: List<Drink>
    ) : CocktailWeatherState

    data class Error(val message: String) : CocktailWeatherState
}