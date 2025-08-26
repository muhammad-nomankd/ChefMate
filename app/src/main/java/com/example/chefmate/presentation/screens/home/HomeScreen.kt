import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.model.RecipeCategory
import com.example.chefmate.presentation.RecipeEvent
import com.example.chefmate.presentation.components.RecipeCard
import com.example.chefmate.presentation.screens.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = hiltViewModel()
) {

    var selectedCategory by remember { mutableStateOf(RecipeCategory.ALL) }

    val categories = RecipeCategory.entries.toTypedArray().take(5)
    val filteredRecipes = if (selectedCategory == RecipeCategory.ALL) {
        recipes
    } else {
        recipes.filter { it.category == selectedCategory }
    }
    val recentRecipes = recipes.take(3)
    val favoriteCount = recipes.count { it.isFavorite }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel) {
        viewModel.event.collect { event ->
            when (event) {
                is RecipeEvent.showToast -> Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    if (uiState.value.isLoading) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    } else {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cookie,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Recipe Book",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Discover delicious recipes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${recipes.size}",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Total Recipes",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "$favoriteCount",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "Favorites",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            item {
                HorizontalDivider(
                    thickness = 0.5.dp, modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }

            if (recentRecipes.isNotEmpty()) {
                item {
                    Column {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Recently Added",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(recentRecipes, key = { it.id }) { recipe ->
                                RecipeCard(
                                    recipe = recipe,
                                    onRecipeClick = onRecipeClick,
                                    onToggleFavorite = onToggleFavorite,
                                    modifier = Modifier.width(250.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Categories
                Column {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            FilterChip(
                                onClick = { selectedCategory = category },
                                label = {
                                    Text(
                                        text = category.displayName,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                },
                                selected = selectedCategory == category,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }

            item {
                // Recipe Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp) // Fixed height for nested scrolling
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredRecipes) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onRecipeClick = onRecipeClick,
                            onToggleFavorite = onToggleFavorite
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val sampleRecipes = listOf(
        Recipe(
            12,
            "Pancakes",
            RecipeCategory.ALL,
            "Breakfast",
            45,
            isFavorite = true,
            servings = 54,
            ingredients = listOf("pasta", "meat", "cheese"),
            instructions = "cook on a slow flame for 30 mins",
            createdAt = 34
        ),
        Recipe(
            14,
            "Spaghetti",
            RecipeCategory.ALL,
            "Dinner",
            45,
            isFavorite = true,
            servings = 54,
            ingredients = listOf("pasta", "meat", "cheese"),
            instructions = "cook on a slow flame for 30 mins",
            createdAt = 34
        ),
        Recipe(
            23,
            "Salad",
            RecipeCategory.ALL,
            "Lunch",
            23,
            isFavorite = true,
            servings = 54,
            ingredients = listOf("pasta", "meat", "cheese"),
            instructions = "cook on a slow flame for 30 mins",
            createdAt = 34
        ),
        Recipe(
            45,
            "Cookies",
            RecipeCategory.ALL,
            "Dessert",
            56,
            servings = 54,
            ingredients = listOf("pasta", "meat", "cheese"),
            instructions = "cook on a slow flame for 30 mins",
            createdAt = 34
        ),
        Recipe(
            234,
            "Omelette",
            RecipeCategory.ALL,
            "Breakfast",
            23,
            servings = 54,
            ingredients = listOf("pasta", "meat", "cheese"),
            instructions = "cook on a slow flame for 30 mins",
            createdAt = 34
        ),
        Recipe(
            34532,
            "Pizza",
            RecipeCategory.ALL,
            "Dinner",
            56,
            servings = 54,
            ingredients = listOf("pasta", "meat", "cheese"),
            instructions = "cook on a slow flame for 30 mins",
            createdAt = 34
        ),
    )
    MaterialTheme {
        Surface {
            HomeScreen(
                recipes = sampleRecipes,
                onRecipeClick = {},
                onToggleFavorite = {},
                paddingValues = PaddingValues()
            )
        }
    }
}

@Preview(showBackground = true, name = "HomeScreen Empty State")
@Composable
fun HomeScreenEmptyPreview() {
    MaterialTheme {
        Surface {
            HomeScreen(
                recipes = emptyList(),
                onRecipeClick = {},
                onToggleFavorite = {},
                paddingValues = PaddingValues()
            )
        }
    }
}
