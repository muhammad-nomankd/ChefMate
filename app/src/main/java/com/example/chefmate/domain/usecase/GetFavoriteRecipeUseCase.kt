package com.example.chefmate.domain.usecase

import com.example.chefmate.domain.repository.RecipeRepository
import javax.inject.Inject

class GetFavoriteRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {
    operator fun invoke() = repository.getFavoriteRecipes()
}