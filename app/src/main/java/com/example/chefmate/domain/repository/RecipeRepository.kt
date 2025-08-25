package com.example.chefmate.domain.repository

import com.example.chefmate.data.local.db.RecipeDao
import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.model.RecipeCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


interface RecipeRepository {
    fun getAllRecipes(): Flow<List<Recipe>>
    fun getFavoriteRecipes(): Flow<List<Recipe>>
    fun getRecipeByCategory(category: RecipeCategory): Flow<List<Recipe>>

    fun searchRecipe(query: String): Flow<List<Recipe>>
    suspend fun getRecipeById(id: Long):Recipe?
    suspend fun updateRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun insertRecipe(recipe: Recipe): Long
    suspend fun toggleFavorite(id: Long)

    fun getTotalRecipesCount(): Flow<Int>
    fun getFavoriteRecipesCount(): Flow<Int>
}