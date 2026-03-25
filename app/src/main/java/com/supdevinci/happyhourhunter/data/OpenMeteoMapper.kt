package com.supdevinci.happyhourhunter.data

fun weatherCodeToDescription(code: Int): String {
    return when (code) {
        0 -> "ciel dégagé"
        1, 2, 3 -> "nuageux"
        45, 48 -> "brouillard"
        51, 53, 55 -> "bruine"
        56, 57 -> "bruine verglaçante"
        61, 63, 65 -> "pluie"
        66, 67 -> "pluie verglaçante"
        71, 73, 75, 77 -> "neige"
        80, 81, 82 -> "averses"
        85, 86 -> "averses de neige"
        95 -> "orage"
        96, 99 -> "orage avec grêle"
        else -> "temps inconnu"
    }
}

fun weatherCodeToMain(code: Int): String {
    return when (code) {
        0 -> "Clear"
        1, 2, 3, 45, 48 -> "Clouds"
        51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82 -> "Rain"
        71, 73, 75, 77, 85, 86 -> "Snow"
        95, 96, 99 -> "Thunderstorm"
        else -> "Clear"
    }
}
