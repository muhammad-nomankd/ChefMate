package com.example.chefmate.data.repository

import com.example.chefmate.data.local.db.RecipeDao
import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.model.RecipeCategory
import com.example.chefmate.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeRepositoryImp @Inject constructor(private val recipeDao: RecipeDao): RecipeRepository {
    override fun getAllRecipes(): Flow<List<Recipe>> =
        recipeDao.getAllRecipes()


    override fun getFavoriteRecipes(): Flow<List<Recipe>> {
         return recipeDao.getFavoriteRecipes()
    }

    override fun getRecipeByCategory(category: RecipeCategory): Flow<List<Recipe>> {
        if (category == RecipeCategory.ALL){
            return recipeDao.getAllRecipes()
        } else {
            return recipeDao.getRecipeByCategory(category)
        }
    }

    override fun searchRecipe(query: String): Flow<List<Recipe>> {
        return recipeDao.searchRecipes(query)
    }

    override suspend fun getRecipeById(id: Long): Recipe? {
        return recipeDao.getRecipeById(id)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.updateRecipe(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }

    override suspend fun insertRecipe(recipe: Recipe): Long {
       return recipeDao.insertRecipe(recipe)
    }

    override suspend fun toggleFavorite(id: Long) {
        recipeDao.toggleFavorite(id)
    }

    override fun getTotalRecipesCount(): Flow<Int> {
      return recipeDao.getTotalRecipesCount()
    }

    override fun getFavoriteRecipesCount(): Flow<Int> {
        return recipeDao.getFavoriteRecipesCount()
    }
}