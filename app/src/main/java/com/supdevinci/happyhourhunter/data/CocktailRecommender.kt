package com.supdevinci.happyhourhunter.data

fun choiceCategoryCocktail(temp: Double, weatherMain: String): String {
    return when {
        weatherMain.contains("Rain", ignoreCase = true) -> "Coffee / Tea"
        weatherMain.contains("Thunderstorm", ignoreCase = true) -> "Shot"
        weatherMain.contains("Snow", ignoreCase = true) -> "Cocoa"
        temp >= 26 -> "Cocktail"
        temp in 18.0..25.9 -> "Ordinary Drink"
        temp in 10.0..17.9 -> "Beer"
        else -> "Coffee / Tea"
    }
}
