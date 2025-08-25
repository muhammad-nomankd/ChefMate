package com.example.chefmate.domain.usecase

import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.repository.RecipeRepository
import javax.inject.Inject

class AddRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {
    suspend operator fun invoke(recipe: Recipe) = repository.insertRecipe(recipe)
}