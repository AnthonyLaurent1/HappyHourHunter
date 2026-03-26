package com.supdevinci.happyhourhunter.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.supdevinci.happyhourhunter.data.local.entities.CocktailEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface CocktailDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cocktail: CocktailEntity): Long

    @Update
    suspend fun update(cocktail: CocktailEntity)

    @Query("UPDATE cocktails SET deletedAt = :date WHERE id = :id")
    suspend fun softDelete(id: Int, date: Date)

    @Query("SELECT * FROM cocktails WHERE deletedAt IS NULL")
    fun getAllVisibleCocktails(): Flow<List<CocktailEntity>>

    @Query("SELECT * FROM cocktails WHERE deletedAt IS NULL AND idDrink = :idDrink LIMIT 1")
    suspend fun getByApiId(idDrink: String): CocktailEntity?

    @Query("SELECT * FROM cocktails WHERE deletedAt IS NULL AND idDrink IN (:ids)")
    suspend fun getByApiIds(ids: List<String>): List<CocktailEntity>

    @Query("SELECT * FROM cocktails WHERE deletedAt IS NULL AND name LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<CocktailEntity>
}
