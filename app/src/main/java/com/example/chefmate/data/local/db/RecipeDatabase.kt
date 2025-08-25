package com.example.chefmate.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chefmate.domain.model.Recipe

@Database(entities = [Recipe::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RecipeDatabase: RoomDatabase() {
    abstract val recipeDao: RecipeDao

    companion object {
        const val DATABASE_NAME  = "recipe_db"
    }
}