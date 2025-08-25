package com.example.chefmate.di

import android.content.Context
import androidx.room.Room
import com.example.chefmate.data.local.db.RecipeDao
import com.example.chefmate.data.local.db.RecipeDatabase
import com.example.chefmate.data.repository.RecipeRepositoryImp
import com.example.chefmate.domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesRecipeDatabase(@ApplicationContext context: Context): RecipeDatabase {
        return Room.databaseBuilder(
            context, RecipeDatabase::class.java, RecipeDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesRecipeDao(database: RecipeDatabase): RecipeDao {
        return database.recipeDao
    }


    @Provides
    @Singleton
    fun bindRecipeRepository(recipeDao: RecipeDao): RecipeRepository {
        return RecipeRepositoryImp(recipeDao)
    }
}