package com.example.chefmate.domain.usecase

import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.repository.RecipeRepository
import javax.inject.Inject

class InsertRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {
    operator suspend fun invoke(recipe: Recipe) = repository.insertRecipe(recipe)
}