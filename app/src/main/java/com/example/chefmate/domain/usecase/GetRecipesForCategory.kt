package com.example.chefmate.domain.usecase

import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.model.RecipeCategory
import com.example.chefmate.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesForCategory @Inject constructor(private val repository: RecipeRepository) {
    operator fun invoke(category: RecipeCategory): Flow<List<Recipe>> = repository.getRecipeByCategory(category)
}