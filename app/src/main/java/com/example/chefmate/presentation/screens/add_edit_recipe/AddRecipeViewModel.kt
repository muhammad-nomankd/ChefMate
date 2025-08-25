package com.example.chefmate.presentation.screens.add_edit_recipe

import android.R.attr.category
import android.R.attr.name
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.model.RecipeCategory
import com.example.chefmate.domain.usecase.AddRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
@HiltViewModel
class AddRecipeViewModel @Inject constructor(private val addRecipeUseCase: AddRecipeUseCase): ViewModel() {

    private val _uiState = MutableStateFlow(AddRecipeUiState())
    val uiState: StateFlow<AddRecipeUiState> = _uiState.asStateFlow()

    fun saveRecipe(
        recipe: Recipe) {

        Log.d("AddRecipe", "saveRecipe called with name: $name")

        val currentState = _uiState.value
        Log.d("AddRecipe", "Current state: $currentState")

        if (recipe.name.isBlank() || recipe.instructions.isBlank() || recipe.ingredients.isEmpty()) {
            Log.e("AddRecipe", "Validation failed")
            _uiState.update { it.copy(errorMessage = "Please fill in all required fields") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val saveRecipe = Recipe(
                    name = recipe.name,
                    category = recipe.category,
                    imageUrl = recipe.imageUrl,
                    cookTimeMinutes = recipe.cookTimeMinutes,
                    servings = recipe.servings,
                    ingredients = recipe.ingredients,
                    instructions = recipe.instructions,
                    isFavorite = recipe.isFavorite,
                    createdAt = recipe.createdAt
                )
                addRecipeUseCase(saveRecipe)
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to save recipe"
                    )
                }
            }
        }
    }

    private fun updateRecipeName(name: String){
        _uiState.update { it.copy(recipeName = name) }
    }
    private fun updateCategory(category: RecipeCategory){
        _uiState.update { it.copy(selectedCategory = category) }
    }
    private fun updateImageUrl(url: String){
        _uiState.update { it.copy(imageUrl = url) }
    }
    private fun updateCookTime(time: String){
        _uiState.update { it.copy(cookTime = time) }
    }
    private fun updateServings(servings: String){
        _uiState.update { it.copy(servings = servings) }
    }
    private fun updateIngredients(ingredients: List<String>){
        _uiState.update { it.copy(ingredients = ingredients) }
    }
    private fun updateInstruction(instruction: String){
        _uiState.update { it.copy(instructions = instruction) }
    }
    private fun updateCurrentIngredient(ingredient: String){
        _uiState.update { it.copy(currentIngredient = ingredient) }
    }
    private fun addIngredient() {
        val currentState = _uiState.value
        if (currentState.currentIngredient.isNotBlank()) {
            _uiState.update {
                it.copy(
                    ingredients = it.ingredients + currentState.currentIngredient.trim(),
                    currentIngredient = ""
                )
            }
        }
    }

    private fun removeIngredient(index: Int) {
        _uiState.update {
            it.copy(ingredients = it.ingredients.filterIndexed { i, _ -> i != index })
        }
    }



    private fun resetForm() {
        _uiState.value = AddRecipeUiState()
    }

    private fun updateInstructions(instructions: String) {
        _uiState.update { it.copy(instructions = instructions) }
    }

    private fun isValidRecipe(state: AddRecipeUiState): Boolean {
        return state.recipeName.isNotBlank() &&
                state.ingredients.isNotEmpty() &&
                state.instructions.isNotBlank() &&
                state.cookTime.toIntOrNull() != null &&
                state.servings.toIntOrNull() != null
    }
}