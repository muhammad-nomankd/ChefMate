package com.example.chefmate.presentation.screens.add_edit_recipe

import com.example.chefmate.domain.model.RecipeCategory

sealed class AddRecipeUiIntent {
    data class UpdateRecipeName(val name: String) : AddRecipeUiIntent()
    data class UpdateCategory(val category: RecipeCategory) : AddRecipeUiIntent()
    data class UpdateImageUrl(val url: String) : AddRecipeUiIntent()
    data class UpdateCookTime(val time: String) : AddRecipeUiIntent()
    data class UpdateServings(val servings: String) : AddRecipeUiIntent()
    data class UpdateCurrentIngredient(val ingredient: String) : AddRecipeUiIntent()
    object AddIngredient : AddRecipeUiIntent()
    data class RemoveIngredient(val index: Int) : AddRecipeUiIntent()
    data class UpdateInstructions(val instructions: String) : AddRecipeUiIntent()
    object SaveRecipe : AddRecipeUiIntent()
    object ResetForm : AddRecipeUiIntent()
}
