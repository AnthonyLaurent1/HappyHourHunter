package com.supdevinci.happyhourhunter.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.supdevinci.happyhourhunter.model.Drink
import com.supdevinci.happyhourhunter.ui.theme.BackgroundCream
import com.supdevinci.happyhourhunter.ui.theme.ErrorRed
import com.supdevinci.happyhourhunter.ui.theme.SurfaceWhite
import com.supdevinci.happyhourhunter.ui.theme.TextPrimary
import com.supdevinci.happyhourhunter.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel,
    onCocktailClick: (String) -> Unit
) {
    val favorites by favoritesViewModel.favorites.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundCream.copy(alpha = 0.72f))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            text = "Favoris",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        if (favorites.isEmpty()) {
            Text("Aucun favori", color = TextPrimary)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
            ) {
                items(favorites) { drink ->
                    FavoriteCocktailCard(
                        drink = drink,
                        onClick = { onCocktailClick(drink.idDrink) },
                        onRemoveFavorite = { favoritesViewModel.removeFromFavorites(drink.idDrink) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteCocktailCard(
    drink: Drink,
    onClick: () -> Unit,
    onRemoveFavorite: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceWhite)
            .clickable { onClick() }
    ) {
        Column {
            AsyncImage(
                model = drink.strDrinkThumb,
                contentDescription = drink.strDrink,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop
            )

            Column(Modifier.padding(12.dp)) {
                Text(
                    text = drink.strDrink,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clip(CircleShape)
                .background(SurfaceWhite.copy(alpha = 0.9f))
                .clickable { onRemoveFavorite() }
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "Retirer des favoris",
                tint = ErrorRed
            )
        }
    }
}
