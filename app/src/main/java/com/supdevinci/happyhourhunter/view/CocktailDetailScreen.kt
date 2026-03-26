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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.supdevinci.happyhourhunter.viewmodel.CocktailDetailViewModel
import com.supdevinci.happyhourhunter.viewmodel.states.CocktailDetailState

@Composable
fun CocktailDetailScreen(
    viewModel: CocktailDetailViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.detailState.collectAsStateWithLifecycle()

    BackHandler {
        onBack()
    }

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
                    color = Color.Red
                )
            }
        }

        is CocktailDetailState.Success -> {
            val drink = currentState.drink
            val ingredients = drink.ingredientsWithMeasures()

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F7F4)),
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
                                .background(Color.White.copy(alpha = 0.9f))
                                .clickable { onBack() }
                                .padding(10.dp)
                                .align(Alignment.TopStart)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Retour",
                                tint = Color.Black
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
                                    DetailTag(glass)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = drink.strDrink,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
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
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "Ingredients",
                                fontWeight = FontWeight.Bold
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
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "Preparation",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(text = drink.strInstructions ?: "Aucune instruction")
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
            .background(Color.White)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFF333333)
        )
    }
}

@Composable
private fun IngredientRow(name: String, measure: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFFFF2F2))
            .padding(14.dp)
    ) {
        Text(
            text = name.trim(),
            fontWeight = FontWeight.Medium
        )

        if (measure.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = measure.trim(),
                color = Color(0xFF666666)
            )
        }
    }
}
