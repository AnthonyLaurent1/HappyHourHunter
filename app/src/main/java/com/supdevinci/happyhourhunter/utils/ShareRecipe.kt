package com.supdevinci.happyhourhunter.utils

import android.content.Context
import android.content.Intent
import com.supdevinci.happyhourhunter.model.Drink

fun shareCocktail(context: Context, drink: Drink) {
    val ingredients = drink.ingredientsWithMeasures()
        .joinToString(separator = "\n") { (ingredient, measure) ->
            val translatedMeasure = measure
                ?.takeIf { it.isNotBlank() }
                ?.let { traductionMeasure(it) }

            if (translatedMeasure.isNullOrBlank()) {
                "- $ingredient"
            } else {
                "- $ingredient : $translatedMeasure"
            }
        }

    val instructions = drink.strInstructionsFR?.takeIf { it.isNotBlank() }
        ?: drink.strInstructions?.takeIf { it.isNotBlank() }
        ?: "Aucune instruction"

    val message = buildString {
        appendLine(drink.strDrink)
        appendLine()

        drink.strCategory?.let {
            appendLine("Categorie : ${traductionCategory(it)}")
        }
        drink.strAlcoholic?.let {
            appendLine("Type : ${traductionType(it)}")
        }
        drink.strGlass?.let {
            appendLine("Verre : ${traductionGlass(it)}")
        }

        appendLine()
        appendLine("Ingredients :")
        appendLine(ingredients)
        appendLine()
        appendLine("Preparation :")
        appendLine(instructions)
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, drink.strDrink)
        putExtra(Intent.EXTRA_TEXT, message)
    }

    context.startActivity(Intent.createChooser(intent, "Partager ce cocktail"))
}
