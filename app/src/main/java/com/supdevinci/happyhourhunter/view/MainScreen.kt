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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.supdevinci.happyhourhunter.model.Drink
import com.supdevinci.happyhourhunter.ui.theme.DarkBanner
import com.supdevinci.happyhourhunter.ui.theme.ErrorRed
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
    val citySearchError by weatherViewModel.citySearchError.collectAsStateWithLifecycle()
    val isCityLoading by weatherViewModel.isCityLoading.collectAsStateWithLifecycle()

    var showCityDialog by remember { mutableStateOf(false) }
    var cityInput by remember { mutableStateOf("") }

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
                Text(
                    text = currentState.message,
                    color = ErrorRed
                )
            }
        }

        is CocktailWeatherState.Success -> {
            LaunchedEffect(currentState.city) {
                if (!showCityDialog) {
                    cityInput = currentState.city
                }
            }

            LaunchedEffect(isCityLoading, citySearchError, currentState.city) {
                if (!isCityLoading && citySearchError == null && showCityDialog) {
                    showCityDialog = false
                }
            }

            if (showCityDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showCityDialog = false
                        weatherViewModel.clearCitySearchError()
                    },
                    title = {
                        Text(
                            text = "Changer de ville",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = cityInput,
                                onValueChange = {
                                    cityInput = it
                                    if (citySearchError != null) {
                                        weatherViewModel.clearCitySearchError()
                                    }
                                },
                                singleLine = true,
                                placeholder = { Text("Ex: Paris") },
                                isError = citySearchError != null,
                                enabled = !isCityLoading,
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (citySearchError != null) {
                                Text(
                                    text = citySearchError!!,
                                    color = ErrorRed,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val started = weatherViewModel.fetchCocktailsForCity(cityInput.trim())
                                if (started && citySearchError == null) {
                                    showCityDialog = false
                                }
                            },
                            enabled = !isCityLoading
                        ) {
                            if (isCityLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.height(18.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Valider")
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showCityDialog = false
                                weatherViewModel.clearCitySearchError()
                                cityInput = currentState.city
                            },
                            enabled = !isCityLoading
                        ) {
                            Text("Annuler")
                        }
                    }
                )
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Column {
                    Text(
                        text = "Découvrir",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Trouvez votre cocktail parfait",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                WeatherHeader(
                    city = currentState.city,
                    weather = currentState.weather,
                    temperature = currentState.temperature,
                    onClick = {
                        cityInput = currentState.city
                        weatherViewModel.clearCitySearchError()
                        showCityDialog = true
                    }
                )

                RecommendationBanner(
                    weather = currentState.weather,
                    temperature = currentState.temperature
                )

                Text(
                    text = "Cocktails populaires pour un temps ${currentState.weather}",
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
private fun WeatherHeader(
    city: String,
    weather: String,
    temperature: Double,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceWhite)
            .clickable(onClick = onClick)
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
            Text(
                text = "${temperature.toInt()}°",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = weather.replaceFirstChar { it.uppercase() },
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = city,
            modifier = Modifier
                .clip(CircleShape)
                .background(TagBackground)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium
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
        Text(
            text = message,
            color = SurfaceWhite,
            style = MaterialTheme.typography.bodyLarge
        )
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
            Text(
                text = drink.strDrink,
                style = MaterialTheme.typography.titleLarge
            )
            drink.strCategory?.let {
                Text(
                    text = traductionCategory(it),
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
