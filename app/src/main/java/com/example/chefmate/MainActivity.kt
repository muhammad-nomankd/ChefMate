package com.example.chefmate

import HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.model.Screen
import com.example.chefmate.presentation.MainViewModel
import com.example.chefmate.presentation.screens.add_edit_recipe.AddRecipeScreen
import com.example.chefmate.presentation.screens.add_edit_recipe.AddRecipeViewModel
import com.example.chefmate.presentation.screens.favorite.FavoritesScreen
import com.example.chefmate.presentation.screens.home.HomeViewModel
import com.example.chefmate.presentation.screens.search.SearchScreen
import com.example.chefmate.presentation.theme.ChefMateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChefMateTheme {
                val addRecipeViewModel: AddRecipeViewModel = hiltViewModel()
                val mainViewModel: MainViewModel = hiltViewModel()
                val homeViewModel: HomeViewModel = hiltViewModel()
                val recipes = homeViewModel.uiState.collectAsState().value.recipes
                val backstack = mainViewModel.backstack
                val categories = homeViewModel.uiState.collectAsState().value.selectedCategory
                val favoriteRecipes = recipes.filter { it.isFavorite }
                val scope = rememberCoroutineScope()


                Scaffold(
                    modifier = Modifier.fillMaxSize(), bottomBar = {
                        BottomAppBar {
                            val currentScreen = backstack.lastOrNull()
                            NavigationBarItem(
                                selected = currentScreen is Screen.Home,
                                onClick = { mainViewModel.navigateTo(Screen.Home) },
                                icon = {
                                    if (currentScreen is Screen.Home) Icon(
                                        Icons.Filled.Home,
                                        contentDescription = "Home",
                                        tint = MaterialTheme.colorScheme.primary
                                    ) else Icon(Icons.Outlined.Home, contentDescription = "Home")
                                })
                            NavigationBarItem(
                                selected = currentScreen is Screen.Search,
                                onClick = { mainViewModel.navigateTo(Screen.Search) },
                                icon = {
                                    if (currentScreen is Screen.Search) Icon(
                                        Icons.Filled.Search,
                                        contentDescription = "Search",
                                        tint = MaterialTheme.colorScheme.primary
                                    ) else Icon(
                                        Icons.Outlined.Search,
                                        contentDescription = "Search"
                                    )
                                })
                            NavigationBarItem(
                                selected = currentScreen is Screen.Add,
                                onClick = { mainViewModel.navigateTo(Screen.Add) }, // Simplified
                                icon = {
                                    if (currentScreen is Screen.Add) Icon(
                                        Icons.Filled.Add,
                                        contentDescription = "Add",
                                        tint = MaterialTheme.colorScheme.primary
                                    ) else Icon(Icons.Outlined.Add, contentDescription = "Add")
                                })
                            NavigationBarItem(
                                selected = currentScreen is Screen.Favorite,
                                onClick = { mainViewModel.navigateTo(Screen.Favorite) },
                                icon = {
                                    if (currentScreen is Screen.Favorite) Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = "Favorite",
                                        tint = MaterialTheme.colorScheme.primary
                                    ) else Icon(
                                        Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Favorite"
                                    )
                                })
                        }
                    }) { paddingValues ->

                    when (backstack.lastOrNull()) {
                        is Screen.Home -> HomeScreen(
                            recipes = recipes,
                            onRecipeClick = {},
                            onToggleFavorite = { recipeId ->
                                homeViewModel.toggleFavorite(recipeId)
                            },
                            paddingValues = paddingValues
                        )

                        is Screen.Search -> SearchScreen(
                            recipes = recipes,
                            selectedCategory = categories, // Dummy data
                            showFilters = true, // Dummy data
                            onSearchQueryChange = { query ->
                                scope.launch {
                                    homeViewModel.searchRecipe(query)
                                }
                            },
                            onCategoryChange = { category ->
                                homeViewModel.selectCategory(category)
                            },
                            onToggleFilters = { /* TODO */ },
                            onClearFilters = { /* TODO */ },
                            onRecipeClick = { /* TODO */ },
                            onToggleFavorite = { id ->
                                homeViewModel.toggleFavorite(id)
                            },
                            paddingValues
                        )

                        is Screen.Add -> AddRecipeScreen(
                            onSaveRecipe = { recipe ->
                                addRecipeViewModel.saveRecipe(
                                    Recipe(
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
                                )
                                mainViewModel.navigateTo(Screen.Home) // Navigate back after saving
                            })

                        is Screen.Favorite -> FavoritesScreen(
                            favoriteRecipes = favoriteRecipes,
                            onRecipeClick = {},
                            onToggleFavorite = { it ->
                                homeViewModel.toggleFavorite(it)
                            },
                            paddingValues = paddingValues
                        )

                        null -> Text("No screen found")
                    }
                }
            }
        }
    }
}
