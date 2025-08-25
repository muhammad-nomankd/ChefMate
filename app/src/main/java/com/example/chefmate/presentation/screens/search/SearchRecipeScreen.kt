package com.example.chefmate.presentation.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.model.RecipeCategory
import com.example.chefmate.presentation.components.RecipeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    recipes: List<Recipe>,
    selectedCategory: RecipeCategory,
    showFilters: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onCategoryChange: (RecipeCategory) -> Unit,
    onToggleFilters: () -> Unit,
    onClearFilters: () -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    paddingValues: PaddingValues
) {
    var searchQuery by remember { mutableStateOf("") }


    val filteredRecipes = recipes.filter { recipe ->
        val matchesSearch = searchQuery.isEmpty() || recipe.name.contains(
            searchQuery,
            ignoreCase = true
        ) || recipe.ingredients.any { ingredient ->
            ingredient.contains(searchQuery, ignoreCase = true)
        }

        val matchesCategory =
            selectedCategory == RecipeCategory.ALL || recipe.category == selectedCategory

        matchesSearch && matchesCategory
    }

    val hasActiveFilters = searchQuery.isNotEmpty() || selectedCategory != RecipeCategory.ALL



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {searchQuery = it},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search recipes by name or ingredient...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = "Search"
                    )
                },
                trailingIcon = if (searchQuery.isNotEmpty()) {
                    {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                } else null,
                singleLine = true,
                shape = MaterialTheme.shapes.large)


            // Filter Options
            if (showFilters) {
                Card(
                    modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(RecipeCategory.entries.toTypedArray()) { category ->
                                FilterChip(
                                    onClick = { onCategoryChange(category) },
                                    label = { Text(category.displayName) },
                                    selected = selectedCategory == category
                                )
                            }
                        }
                    }
                }
            }

            // Active Filters Display
            if (hasActiveFilters) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (searchQuery.isNotEmpty()) {
                        item {
                            AssistChip(
                                onClick = { searchQuery = ""},
                                label = { Text("Search: \"$searchQuery\"") },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Remove",
                                        modifier = Modifier.size(16.dp)
                                    )
                                })
                        }
                    }

                    if (selectedCategory != RecipeCategory.ALL) {
                        item {
                            AssistChip(
                                onClick = { onCategoryChange(RecipeCategory.ALL)
                                          searchQuery = ""},
                                label = { Text(selectedCategory.displayName) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Remove",
                                        modifier = Modifier.size(16.dp)
                                    )
                                })
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Results
        Text(
            text = "${filteredRecipes.size} recipe${if (filteredRecipes.size != 1) "s" else ""} found",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (filteredRecipes.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp, bottom = 80.dp // Account for bottom navigation
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredRecipes, key = {it.id }) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onRecipeClick = onRecipeClick,
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }
        } else {
            // No results state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "No recipes found",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Try adjusting your search terms or filters",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}