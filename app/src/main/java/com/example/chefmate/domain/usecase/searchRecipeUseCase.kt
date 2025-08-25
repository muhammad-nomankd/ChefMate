package com.example.chefmate.domain.usecase

import androidx.room.Query
import com.example.chefmate.domain.repository.RecipeRepository
import javax.inject.Inject

class SearchRecipeUseCase @Inject constructor(private val repository: RecipeRepository) {
    operator fun invoke(query: String) = repository.searchRecipe(query)
}