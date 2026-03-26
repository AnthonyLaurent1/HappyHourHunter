package com.supdevinci.happyhourhunter.data.local

import com.supdevinci.happyhourhunter.data.local.entities.CocktailEntity
import com.supdevinci.happyhourhunter.model.Drink
import java.util.Date

fun Drink.toEntity(): CocktailEntity {
    return CocktailEntity(
        idDrink = idDrink,
        name = strDrink,
        category = strCategory,
        alcoholic = strAlcoholic,
        glass = strGlass,
        instructions = strInstructions,
        thumbnail = strDrinkThumb,
        ingredient1 = strIngredient1,
        ingredient2 = strIngredient2,
        ingredient3 = strIngredient3,
        ingredient4 = strIngredient4,
        ingredient5 = strIngredient5,
        ingredient6 = strIngredient6,
        ingredient7 = strIngredient7,
        ingredient8 = strIngredient8,
        ingredient9 = strIngredient9,
        ingredient10 = strIngredient10,
        measure1 = strMeasure1,
        measure2 = strMeasure2,
        measure3 = strMeasure3,
        measure4 = strMeasure4,
        measure5 = strMeasure5,
        measure6 = strMeasure6,
        measure7 = strMeasure7,
        measure8 = strMeasure8,
        measure9 = strMeasure9,
        measure10 = strMeasure10,
        createdAt = Date()
    )
}

fun CocktailEntity.toDrink(): Drink {
    return Drink(
        idDrink = idDrink,
        strDrink = name,
        strCategory = category,
        strAlcoholic = alcoholic,
        strGlass = glass,
        strInstructions = instructions,
        strDrinkThumb = thumbnail,
        strIngredient1 = ingredient1,
        strIngredient2 = ingredient2,
        strIngredient3 = ingredient3,
        strIngredient4 = ingredient4,
        strIngredient5 = ingredient5,
        strIngredient6 = ingredient6,
        strIngredient7 = ingredient7,
        strIngredient8 = ingredient8,
        strIngredient9 = ingredient9,
        strIngredient10 = ingredient10,
        strMeasure1 = measure1,
        strMeasure2 = measure2,
        strMeasure3 = measure3,
        strMeasure4 = measure4,
        strMeasure5 = measure5,
        strMeasure6 = measure6,
        strMeasure7 = measure7,
        strMeasure8 = measure8,
        strMeasure9 = measure9,
        strMeasure10 = measure10
    )
}
