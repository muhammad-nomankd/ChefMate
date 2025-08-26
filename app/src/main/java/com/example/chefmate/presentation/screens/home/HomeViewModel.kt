package com.example.chefmate.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.model.RecipeCategory
import com.example.chefmate.domain.repository.RecipeRepository
import com.example.chefmate.domain.usecase.GetAllRecipeUseCase
import com.example.chefmate.domain.usecase.GetFavoriteRecipeUseCase
import com.example.chefmate.domain.usecase.GetRecipesForCategory
import com.example.chefmate.domain.usecase.SearchRecipeUseCase
import com.example.chefmate.domain.usecase.ToggleFavoriteUseCase
import com.example.chefmate.presentation.RecipeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val getAllRecipesUseCase: GetAllRecipeUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getCategoriesUseCase: GetRecipesForCategory,
    private val getFavoritesRecipesUseCase: GetFavoriteRecipeUseCase,
    private val searchRecipesUseCase: SearchRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<RecipeEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<RecipeEvent> = _event.asSharedFlow()

    init {
        handleUiIntent(HomeUiIntent.LoadRecipes)
    }

    fun handleUiIntent(intent: HomeUiIntent) {
        when (intent) {
            is HomeUiIntent.LoadRecipes -> loadRecipes()
            is HomeUiIntent.SelectCategory -> selectCategory(intent.category)
            is HomeUiIntent.ToggleFavorite -> toggleFavorite(intent.id)
        }

    }

    val recipes: StateFlow<List<Recipe>> = repository.getAllRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

     fun selectCategory(category: RecipeCategory) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedCategory = category) }
            getCategoriesUseCase(category)
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch {
                    _uiState.update { it.copy(errorMessage = it.errorMessage) }
                }
                .collect { recipes ->
                    _event.emit(RecipeEvent.showToast("Category selected"))
                    _uiState.update { it.copy(recipes = recipes) }
                }
        }
    }

     fun toggleFavorite(recipeId: Long) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(recipeId)
                _event.emit(RecipeEvent.showToast("Favorite toggled"))

            } catch (e: Exception) {
                _event.emit(RecipeEvent.showToast("Error toggling favorite"))
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            combine(
                getAllRecipesUseCase(),
                getFavoritesRecipesUseCase()
            ) { allRecipes, favoriteRecipes ->
                val currentCategory = _uiState.value.selectedCategory
                val filteredRecipes = if (currentCategory == RecipeCategory.ALL) {
                    allRecipes
                } else {
                    allRecipes.filter { it.category == currentCategory}
                }

                _uiState.update {
                    _event.emit(RecipeEvent.showToast("Recipes loaded"))
                    it.copy(
                        recipes = filteredRecipes,
                        totalRecipes = allRecipes.size,
                        favoriteRecipes = favoriteRecipes.size,
                        isLoading = false
                    )
                }
            }.collect()
        }
    }

    suspend fun getFavoriteRecipes(){
        viewModelScope.launch {
            getFavoritesRecipesUseCase().collect { favoriteRecipes ->
             _uiState.update { it.copy(
                 recipes = favoriteRecipes,
                 favoriteRecipes = favoriteRecipes.size
             ) }
            }
        }

    }

    suspend fun searchRecipe(query: String)
    {
        viewModelScope.launch {
            searchRecipesUseCase(query).collect { searchedRecipes ->
                _uiState.update { it.copy(
                    recipes =  searchedRecipes
                ) }
            }
        }
    }
}