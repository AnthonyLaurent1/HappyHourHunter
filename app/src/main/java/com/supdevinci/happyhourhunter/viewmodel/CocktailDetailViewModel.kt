package com.supdevinci.happyhourhunter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.happyhourhunter.data.RetrofitInstance
import com.supdevinci.happyhourhunter.data.local.CocktailDatabase
import com.supdevinci.happyhourhunter.data.local.toDrink
import com.supdevinci.happyhourhunter.data.local.toEntity
import com.supdevinci.happyhourhunter.viewmodel.states.CocktailDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CocktailDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val cocktailDao = CocktailDatabase.getDatabase(application).cocktailDao()
    private val _detailState = MutableStateFlow<CocktailDetailState>(CocktailDetailState.Loading)
    val detailState: StateFlow<CocktailDetailState> = _detailState.asStateFlow()

    fun fetchCocktailDetail(id: String) {
        viewModelScope.launch {
            _detailState.value = CocktailDetailState.Loading

            try {
                val localCocktail = cocktailDao.getByApiId(id)

                if (localCocktail != null) {
                    _detailState.value = CocktailDetailState.Success(localCocktail.toDrink())
                    return@launch
                }

                val response = RetrofitInstance.cocktailApi.getCocktailDetail(id)
                val drink = response.drinks.firstOrNull()

                if (drink != null) {
                    cocktailDao.insert(drink.toEntity())
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
