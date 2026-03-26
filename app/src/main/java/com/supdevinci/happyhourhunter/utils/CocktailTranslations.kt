package com.supdevinci.happyhourhunter.utils

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

fun traductionGlass(glass: String): String {
    return when (glass.trim()) {
        "Balloon Glass" -> "Verre ballon"
        "Beer Glass" -> "Verre a biere"
        "Beer mug" -> "Chope de biere"
        "Beer pilsner" -> "Verre pilsner"
        "Brandy snifter" -> "Verre a brandy"
        "Champagne flute" -> "Flute a champagne"
        "Cocktail glass" -> "Verre a cocktail"
        "Coffee mug" -> "Mug"
        "Collins glass" -> "Verre Collins"
        "Copper Mug" -> "Mug en cuivre"
        "Cordial glass" -> "Verre a liqueur"
        "Coupe Glass" -> "Verre coupe"
        "Highball glass" -> "Verre highball"
        "Hurricane glass" -> "Verre hurricane"
        "Irish coffee cup" -> "Tasse irish coffee"
        "Jar" -> "Bocal"
        "Margarita glass" -> "Verre a margarita"
        "Margarita/Coupette glass" -> "Verre a margarita ou coupette"
        "Martini Glass" -> "Verre a martini"
        "Mason jar" -> "Bocal Mason"
        "Nick and Nora Glass" -> "Verre Nick and Nora"
        "Old-fashioned glass" -> "Verre old-fashioned"
        "Parfait glass" -> "Verre a parfait"
        "Pint glass" -> "Verre pinte"
        "Pitcher" -> "Pichet"
        "Pousse cafe glass" -> "Verre a pousse-cafe"
        "Punch bowl" -> "Bol a punch"
        "Shot glass" -> "Verre a shot"
        "Whiskey Glass" -> "Verre a whisky"
        "Whiskey sour glass" -> "Verre a whiskey sour"
        "White wine glass" -> "Verre a vin blanc"
        "Wine Glass" -> "Verre a vin"
        else -> glass
    }
}

fun traductionMeasure(measure: String): String {
    val value = measure.trim()

    if (value.isBlank()) return value

    return value
        .replace(" dashes", " traits", ignoreCase = true)
        .replace(" dash", " trait", ignoreCase = true)
        .replace(" drops", " gouttes", ignoreCase = true)
        .replace(" drop", " goutte", ignoreCase = true)
        .replace(" teaspoons", " cuilleres a cafe", ignoreCase = true)
        .replace(" teaspoon", " cuillere a cafe", ignoreCase = true)
        .replace(" tsp", " cuillere a cafe", ignoreCase = true)
        .replace(" tablespoons", " cuilleres a soupe", ignoreCase = true)
        .replace(" tablespoon", " cuillere a soupe", ignoreCase = true)
        .replace(" tblsp", " cuillere a soupe", ignoreCase = true)
        .replace(" tbsp", " cuillere a soupe", ignoreCase = true)
        .replace(" cups", " tasses", ignoreCase = true)
        .replace(" cup", " tasse", ignoreCase = true)
        .replace(" splash", " trait", ignoreCase = true)
        .replace(" oz", " cl", ignoreCase = true)
        .replace(" ml", " ml", ignoreCase = true)
        .replace(" pint hard", " Pinte dur", ignoreCase = true)
        .replace(" pint sweet or dry", " Pinte doux ou sec", ignoreCase = true)
        .replace(" pint", " Pinte", ignoreCase = true)
        .replace(" bottle", " Bouteille", ignoreCase = true)
        .replace(" Juice of", " Jus de", ignoreCase = true)
        .replace(" Chilled", " Glacé", ignoreCase = true)
        .replace(" a little bit of", " un peu de", ignoreCase = true)
        .replace(" handful", " poignée", ignoreCase = true)
        .trim()
}






