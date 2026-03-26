package com.supdevinci.happyhourhunter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.happyhourhunter.data.local.CocktailDatabase
import com.supdevinci.happyhourhunter.data.local.toDrink
import com.supdevinci.happyhourhunter.model.Drink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val cocktailDao = CocktailDatabase.getDatabase(application).cocktailDao()

    private val _favorites = MutableStateFlow<List<Drink>>(emptyList())
    val favorites: StateFlow<List<Drink>> = _favorites.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            cocktailDao.getFavoriteCocktails().collect { cocktails ->
                _favorites.value = cocktails.map { it.toDrink() }
            }
        }
    }

    fun removeFromFavorites(idDrink: String) {
        viewModelScope.launch {
            cocktailDao.updateFavoriteStatus(
                idDrink = idDrink,
                isFavorite = false,
                updatedAt = Date()
            )
        }
    }
}
