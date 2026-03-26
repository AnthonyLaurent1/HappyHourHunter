package com.supdevinci.happyhourhunter.viewmodel.states

import com.supdevinci.happyhourhunter.model.Drink

sealed interface CocktailState {
    data object Loading : CocktailState
    data class Success(val drink: Drink) : CocktailState
    data class Error(val message: String) : CocktailState
}