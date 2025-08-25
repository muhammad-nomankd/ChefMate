package com.example.chefmate.presentation.screens.add_edit_recipe

import com.example.chefmate.domain.model.RecipeCategory

data class AddRecipeUiState(
    val recipeName: String = "",
    val selectedCategory: RecipeCategory = RecipeCategory.DINNER,
    val imageUrl: String = "",
    val cookTime: String = "",
    val servings: String = "",
    val ingredients: List<String> = emptyList(),
    val instructions: String = "",
    val currentIngredient: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSaved: Boolean = false
)