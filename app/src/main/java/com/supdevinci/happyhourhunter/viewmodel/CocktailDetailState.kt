package com.supdevinci.happyhourhunter.viewmodel

import com.supdevinci.happyhourhunter.model.Drink

sealed interface CocktailDetailState {
    data object Loading : CocktailDetailState
    data class Success(val drink: Drink) : CocktailDetailState
    data class Error(val message: String) : CocktailDetailState
}
