package com.supdevinci.happyhourhunter.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.supdevinci.happyhourhunter.ui.theme.BackgroundCream
import com.supdevinci.happyhourhunter.ui.theme.ErrorRed
import com.supdevinci.happyhourhunter.ui.theme.SurfaceSoftPink
import com.supdevinci.happyhourhunter.ui.theme.SurfaceWhite
import com.supdevinci.happyhourhunter.ui.theme.TextPrimary
import com.supdevinci.happyhourhunter.ui.theme.TextSecondary
import com.supdevinci.happyhourhunter.utils.traductionCategory
import com.supdevinci.happyhourhunter.utils.traductionGlass
import com.supdevinci.happyhourhunter.utils.traductionMeasure
import com.supdevinci.happyhourhunter.utils.traductionType
import com.supdevinci.happyhourhunter.viewmodel.CocktailDetailViewModel
import com.supdevinci.happyhourhunter.viewmodel.states.CocktailDetailState

@Composable
fun CocktailDetailScreen(
    viewModel: CocktailDetailViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.detailState.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    BackHandler { onBack() }

    when (val currentState = state) {
        is CocktailDetailState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is CocktailDetailState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentState.message,
                    color = ErrorRed
                )
            }
        }

        is CocktailDetailState.Success -> {
            val drink = currentState.drink
            val ingredients = drink.ingredientsWithMeasures()

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(BackgroundCream),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Box {
                        AsyncImage(
                            model = drink.strDrinkThumb,
                            contentDescription = drink.strDrink,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .statusBarsPadding()
                                .padding(16.dp)
                                .clip(CircleShape)
                                .background(SurfaceWhite.copy(alpha = 0.9f))
                                .clickable { onBack() }
                                .padding(10.dp)
                                .align(Alignment.TopStart)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Retour",
                                tint = TextPrimary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .statusBarsPadding()
                                .padding(16.dp)
                                .clip(CircleShape)
                                .background(SurfaceWhite.copy(alpha = 0.9f))
                                .clickable { viewModel.toggleFavorite() }
                                .padding(10.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) {
                                    Icons.Outlined.Favorite
                                } else {
                                    Icons.Outlined.FavoriteBorder
                                },
                                contentDescription = "Favori",
                                tint = if (isFavorite) ErrorRed else TextPrimary
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(16.dp)
                                .align(Alignment.BottomStart)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                drink.strCategory?.let { category ->
                                    DetailTag(traductionCategory(category))
                                }
                                drink.strAlcoholic?.let { type ->
                                    DetailTag(traductionType(type))
                                }
                                drink.strGlass?.let { glass ->
                                    DetailTag(traductionGlass(glass))
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = drink.strDrink,
                                color = SurfaceWhite,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "Ingredients",
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            ingredients.forEach { item ->
                                IngredientRow(
                                    name = item.first,
                                    measure = item.second ?: ""
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "Preparation",
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = drink.strInstructionsFR?.takeIf { it.isNotBlank() }
                                    ?: drink.strInstructions?.takeIf { it.isNotBlank() }
                                    ?: "Aucune instruction",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailTag(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceWhite)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = TextPrimary
        )
    }
}

@Composable
private fun IngredientRow(name: String, measure: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceSoftPink)
            .padding(14.dp)
    ) {
        Text(
            text = name.trim(),
            fontWeight = FontWeight.Medium
        )

        if (measure.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = traductionMeasure(measure.trim()),
                color = TextSecondary
            )
        }
    }
}
