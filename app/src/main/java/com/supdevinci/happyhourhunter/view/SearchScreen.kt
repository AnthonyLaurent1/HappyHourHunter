package com.supdevinci.happyhourhunter.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.supdevinci.happyhourhunter.viewmodel.CocktailSearchViewModel

@Composable
fun SearchScreen(searchViewModel: CocktailSearchViewModel, onCocktailClick: (String) -> Unit) {
    val searchResults by searchViewModel.searchResults.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color(0xFFF8F7F4))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text("Recherche")

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                searchViewModel.searchCocktails(it)
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Outlined.Search, contentDescription = null)
            },
            placeholder = { Text("Rechercher un cocktail...") },
            shape = RoundedCornerShape(18.dp)
        )

        if (searchResults.isEmpty() && searchQuery.isNotBlank()) {
            Text("Aucun cocktail trouvé")
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().height(500.dp)
        ) {
            items(searchResults) { drink ->
                CocktailCard(drink = drink) {
                    onCocktailClick(drink.idDrink)
                }
            }
        }
    }
}
