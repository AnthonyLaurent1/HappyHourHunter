package com.supdevinci.happyhourhunter.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.supdevinci.happyhourhunter.data.local.entities.CocktailEntity

@Dao
interface CocktailDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cocktail: CocktailEntity): Long

    @Query("SELECT * FROM cocktails WHERE deletedAt IS NULL ORDER BY name ASC")
    suspend fun getAllCocktails(): List<CocktailEntity>

    @Query("SELECT * FROM cocktails WHERE deletedAt IS NULL AND idDrink = :idDrink LIMIT 1")
    suspend fun getByApiId(idDrink: String): CocktailEntity?

    @Query("SELECT * FROM cocktails WHERE deletedAt IS NULL AND name LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<CocktailEntity>

    @Query("SELECT * FROM cocktails WHERE deletedAt IS NULL AND isFavorite = 1")
    fun getFavoriteCocktails(): kotlinx.coroutines.flow.Flow<List<CocktailEntity>>

    @Query("UPDATE cocktails SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE idDrink = :idDrink")
    suspend fun updateFavoriteStatus(
        idDrink: String,
        isFavorite: Boolean,
        updatedAt: java.util.Date
    )

}
