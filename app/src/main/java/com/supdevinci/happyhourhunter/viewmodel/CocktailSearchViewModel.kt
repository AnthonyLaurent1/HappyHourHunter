package com.supdevinci.happyhourhunter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.happyhourhunter.data.RetrofitInstance
import com.supdevinci.happyhourhunter.data.local.CocktailDatabase
import com.supdevinci.happyhourhunter.data.local.toDrink
import com.supdevinci.happyhourhunter.data.local.toEntity
import com.supdevinci.happyhourhunter.model.Drink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CocktailSearchViewModel(application: Application) : AndroidViewModel(application) {

    private val cocktailDao = CocktailDatabase.getDatabase(application).cocktailDao()
    private val _searchResults = MutableStateFlow<List<Drink>>(emptyList())
    val searchResults: StateFlow<List<Drink>> = _searchResults.asStateFlow()

    init {
        loadAllCocktails()
    }

    fun loadAllCocktails() {
        viewModelScope.launch {
            _searchResults.value = cocktailDao.getAllCocktails().map { it.toDrink() }
        }
    }
    fun searchCocktails(name: String) {
        viewModelScope.launch {
            if (name.isBlank()) {
                _searchResults.value = cocktailDao.getAllCocktails().map { it.toDrink() }
                return@launch
            }

            val localResults = cocktailDao.searchByName(name)

            if (localResults.isNotEmpty()) {
                _searchResults.value = localResults.map { it.toDrink() }
                return@launch
            }

            try {
                val response = RetrofitInstance.cocktailApi.searchCocktailsByName(name)
                val apiResults = response.drinks

                apiResults.forEach { drink ->
                    if (cocktailDao.getByApiId(drink.idDrink) == null) {
                        cocktailDao.insert(drink.toEntity())
                    }
                }

                _searchResults.value = cocktailDao.searchByName(name).map { it.toDrink() }
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            }
        }
    }
}
