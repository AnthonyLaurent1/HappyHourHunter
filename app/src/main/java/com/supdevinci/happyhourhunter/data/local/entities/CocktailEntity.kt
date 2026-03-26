package com.supdevinci.happyhourhunter.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "cocktails")
data class CocktailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idDrink: String,
    val name: String,
    val category: String?,
    val alcoholic: String?,
    val glass: String?,
    val instructions: String?,
    val thumbnail: String?,
    val ingredient1: String?,
    val ingredient2: String?,
    val ingredient3: String?,
    val ingredient4: String?,
    val ingredient5: String?,
    val ingredient6: String?,
    val ingredient7: String?,
    val ingredient8: String?,
    val ingredient9: String?,
    val ingredient10: String?,
    val measure1: String?,
    val measure2: String?,
    val measure3: String?,
    val measure4: String?,
    val measure5: String?,
    val measure6: String?,
    val measure7: String?,
    val measure8: String?,
    val measure9: String?,
    val measure10: String?,
    val isFavorite: Boolean = false,
    val createdAt: Date,
    val updatedAt: Date? = null,
    val deletedAt: Date? = null
)
