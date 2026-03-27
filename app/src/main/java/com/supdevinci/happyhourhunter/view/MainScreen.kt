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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.items
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
import com.supdevinci.happyhourhunter.ui.theme.TextPrimary
import com.supdevinci.happyhourhunter.ui.theme.TextSecondary
import com.supdevinci.happyhourhunter.utils.traductionCategory
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
            LoadingContent(modifier = modifier)
        }

        is CocktailWeatherState.Error -> {
            ErrorContent(message = currentState.message, modifier = modifier)
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
                ChangeCityDialog(
                    cityInput = cityInput,
                    citySearchError = citySearchError,
                    isCityLoading = isCityLoading,
                    onCityInputChange = {
                        cityInput = it
                        if (citySearchError != null) {
                            weatherViewModel.clearCitySearchError()
                        }
                    },
                    onConfirm = {
                        weatherViewModel.fetchCocktailsForCity(cityInput.trim())
                    },
                    onDismiss = {
                        showCityDialog = false
                        weatherViewModel.clearCitySearchError()
                    },
                    onCancel = {
                        showCityDialog = false
                        weatherViewModel.clearCitySearchError()
                        cityInput = currentState.city
                    }
                )
            }

            MainSuccessContent(state = currentState, modifier = modifier, onWeatherHeaderClick = {
                    cityInput = currentState.city
                    weatherViewModel.clearCitySearchError()
                    showCityDialog = true
                },
                onCocktailClick = onCocktailClick
            )
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = ErrorRed)
    }
}

@Composable
private fun MainSuccessContent(
    state: CocktailWeatherState.Success,
    modifier: Modifier = Modifier,
    onWeatherHeaderClick: () -> Unit,
    onCocktailClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.55f))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
    ScreenHeader()

        WeatherHeader(
            city = state.city,
            weather = state.weather,
            temperature = state.temperature,
            onClick = onWeatherHeaderClick
        )

        RecommendationBanner(
            weather = state.weather,
            temperature = state.temperature
        )

        Text(
            text = "Cocktails populaires pour un temps ${state.weather}",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )

        CocktailGrid(cocktails = state.cocktails.take(4), onCocktailClick = onCocktailClick)
    }
}

@Composable
private fun ScreenHeader() {
    Column {
        Text(text = "Découvrir", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
        Text(
            text = "Trouvez votre cocktail parfait",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

@Composable
private fun ChangeCityDialog(
    cityInput: String,
    citySearchError: String?,
    isCityLoading: Boolean,
    onCityInputChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkBanner,
        titleContentColor = TextPrimary,
        textContentColor = TextPrimary,
        title = {
            Text(
                text = "Changer de ville",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = cityInput,
                    onValueChange = onCityInputChange,
                    singleLine = true,
                    placeholder = { Text("Ex: Paris") },
                    isError = citySearchError != null,
                    enabled = !isCityLoading,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (citySearchError != null) {
                    Text(
                        text = citySearchError,
                        color = ErrorRed,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = !isCityLoading) {
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
            TextButton(onClick = onCancel, enabled = !isCityLoading) {
                Text("Annuler")
            }
        }
    )
}

@Composable
private fun CocktailGrid(cocktails: List<Drink>, onCocktailClick: (String) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(cocktails.take(4)) { drink ->
            CocktailCardCarousel(drink = drink) {
                onCocktailClick(drink.idDrink)
            }
        }
    }
}


@Composable
private fun CocktailCardCarousel(drink: Drink, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(220.dp)
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
                .height(180.dp),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.padding(12.dp)) {
            Text(
                text = drink.strDrink,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
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
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
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
        Text(text = message, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
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
            Text(text = drink.strDrink, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
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
