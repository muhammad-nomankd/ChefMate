package com.example.chefmate.presentation.screens.home

import com.example.chefmate.domain.model.RecipeCategory

sealed class HomeUiIntent {
    object LoadRecipes: HomeUiIntent()
    data class SelectCategory(val category: RecipeCategory): HomeUiIntent()
    data class ToggleFavorite(val id:Long): HomeUiIntent()
}