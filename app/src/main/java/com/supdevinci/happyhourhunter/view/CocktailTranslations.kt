package com.supdevinci.happyhourhunter.view

fun traductionType(type: String?): String {
    return when (type) {
        "Alcoholic" -> "Alcoolisé"
        "Non alcoholic" -> "Sans alcool"
        "Optional alcohol" -> "Alcool optionnel"
        null -> "Inconnu"
        else -> type
    }
}

fun traductionCategory(category: String?): String {
    return when (category) {
        "Beer" -> "Bière"
        "Cocktail" -> "Cocktail"
        "Cocoa" -> "Cacao"
        "Coffee / Tea" -> "Café / Thé"
        "Homemade Liqueur" -> "Liqueur maison"
        "Ordinary Drink" -> "Boisson classique"
        "Other / Unknown" -> "Autre / Inconnu"
        "Punch / Party Drink" -> "Punch / Boisson de fête"
        "Shake" -> "Milk-shake"
        "Shot" -> "Shot"
        "Soft Drink" -> "Boisson sans alcool"
        null -> "Inconnue"
        else -> category
    }
}


