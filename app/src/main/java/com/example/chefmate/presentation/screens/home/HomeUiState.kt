package com.example.chefmate.presentation.screens.home

import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.model.RecipeCategory

data class HomeUiState(
    val recipes: List<Recipe> = emptyList(),
    val selectedCategory: RecipeCategory = RecipeCategory.ALL,
    val totalRecipes: Int = 0,
    val favoriteRecipes: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)