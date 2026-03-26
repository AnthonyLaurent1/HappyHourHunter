package com.supdevinci.happyhourhunter.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.supdevinci.happyhourhunter.data.local.converters.Converter
import com.supdevinci.happyhourhunter.data.local.dao.CocktailDao
import com.supdevinci.happyhourhunter.data.local.entities.CocktailEntity

@Database(
    entities = [CocktailEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class CocktailDatabase : RoomDatabase() {

    abstract fun cocktailDao(): CocktailDao

    companion object {
        @Volatile
        private var INSTANCE: CocktailDatabase? = null

        fun getDatabase(context: Context): CocktailDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CocktailDatabase::class.java,
                    "cocktail_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
