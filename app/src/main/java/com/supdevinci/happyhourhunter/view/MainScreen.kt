package com.supdevinci.happyhourhunter.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.supdevinci.happyhourhunter.model.Drink
import com.supdevinci.happyhourhunter.ui.theme.DarkBanner
import com.supdevinci.happyhourhunter.ui.theme.SurfaceWhite
import com.supdevinci.happyhourhunter.ui.theme.TagBackground
import com.supdevinci.happyhourhunter.ui.theme.TextSecondary
import com.supdevinci.happyhourhunter.viewmodel.WeatherCocktailViewModel
import com.supdevinci.happyhourhunter.viewmodel.states.CocktailWeatherState

@Composable
fun MainScreen(
    weatherViewModel: WeatherCocktailViewModel,
    modifier: Modifier = Modifier,
    onCocktailClick: (String) -> Unit
) {
    val state by weatherViewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state) {
        is CocktailWeatherState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is CocktailWeatherState.Error -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(currentState.message, color = MaterialTheme.colorScheme.error)
            }
        }

        is CocktailWeatherState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Column {
                    Text(text = "Découvrir", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = "Trouvez votre cocktail parfait",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                }

                WeatherHeader(
                    city = currentState.city,
                    weather = currentState.weather,
                    temperature = currentState.temperature
                )

                RecommendationBanner(
                    weather = currentState.weather,
                    temperature = currentState.temperature
                )

                Text(
                    text = "Cocktails populaires temps : ${currentState.weather}",
                    style = MaterialTheme.typography.titleLarge
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp)
                ) {
                    items(currentState.cocktails.take(4)) { drink ->
                        CocktailCard(drink = drink) {
                            onCocktailClick(drink.idDrink)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherHeader(city: String, weather: String, temperature: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Cloud,
            contentDescription = null,
            tint = TextSecondary
        )

        Spacer(modifier = Modifier.padding(horizontal = 6.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text("${temperature.toInt()}°", fontWeight = FontWeight.Bold)
            Text(weather.replaceFirstChar { it.uppercase() },  color = TextSecondary)
        }

        Text(
            text = city,
            modifier = Modifier
                .clip(CircleShape)
                .background(TagBackground)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            color = TextSecondary
        )
    }
}

@Composable
private fun RecommendationBanner(weather: String, temperature: Double) {
    val message = when {
        weather.contains("pluie", ignoreCase = true) ->
            "Il pleut ? Réchauffez-vous avec un bon cocktail"
        temperature >= 25 ->
            "Il fait chaud ? Prenez un cocktail bien frais"
        temperature <= 10 ->
            "Temps froid ? Optez pour quelque chose de réconfortant"
        else ->
            "Le moment parfait pour découvrir un nouveau cocktail"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(DarkBanner)
            .padding(22.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(message, color = SurfaceWhite, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun CocktailCard(drink: Drink, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(bottom = 12.dp)
    ) {
        AsyncImage(
            model = drink.strDrinkThumb,
            contentDescription = drink.strDrink,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.padding(12.dp)) {
            Text(text = drink.strDrink, style = MaterialTheme.typography.titleLarge)
            drink.strCategory?.let {
                Text(traductionCategory(it), color = TextSecondary)
            }
        }
    }
}
