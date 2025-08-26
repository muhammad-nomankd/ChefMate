package com.example.chefmate.presentation.components

import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import coil3.compose.AsyncImage
import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.model.RecipeCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(
    recipe: Recipe,
    onRecipeClick: (Recipe) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onRecipeClick(recipe) },
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp), // Reduced height
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp, pressedElevation = 12.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp) // Reduced image height
            ) {
                if (recipe.imageUrl != null && recipe.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = recipe.imageUrl,
                        contentDescription = recipe.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }

                // Category badge
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = recipe.category.name,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start =8.dp),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        labelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )

                // Favorite button
                IconButton(
                    onClick = { onToggleFavorite(recipe.id.toLong()) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.8f),
                            shape = CircleShape
                        )
                        .size(28.dp)
                ) {
                    Icon(
                        imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (recipe.isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (recipe.isFavorite) MaterialTheme.colorScheme.primary else Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Content section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp), // Reduced padding
                verticalArrangement = Arrangement.spacedBy(8.dp) // Reduced spacing
            ) {
                // Recipe name
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleSmall, // Smaller title
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Cook time and servings
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Cook time",
                            modifier = Modifier.size(14.dp), // Smaller icons
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${recipe.cookTimeMinutes}m",
                            style = MaterialTheme.typography.labelSmall, // Smaller text
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = "Servings",
                            modifier = Modifier.size(14.dp), // Smaller icons
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${recipe.servings}",
                            style = MaterialTheme.typography.labelSmall, // Smaller text
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (recipe.ingredients.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                    ) {
                        items(recipe.ingredients, key = {it}) { ingredient ->
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                Box(modifier = Modifier.padding(4.dp)
                                    .border(
                                        shape = RoundedCornerShape(4.dp), width = 0.5.dp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    ), contentAlignment = Alignment.Center){
                                    Text(
                                        text = ingredient,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp) // control spacing
                                    )
                                }

                            }
                        }
                        if (recipe.ingredients.size > 5) {
                            item {
                                AssistChip(
                                    onClick = { },
                                    label = {
                                        Text(
                                            text = "+${recipe.ingredients.size - 3}",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        labelColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RecipeCardPreview() {
    RecipeCard(
        recipe = Recipe(
            id = 1,
            name = "Bibim-Guksu (Korean Spicy Cold Noodles)",
            imageUrl = "https://www.themealdb.com/images/media/meals/sytuqu1511553755.jpg",
            category = RecipeCategory.DINNER,
            cookTimeMinutes = 15,
            servings = 3,
            ingredients = listOf(
                "¼ cup sesame paste",
                "Ground Beef",
                "Tomato Sauce",
                "Onion",
                "Garlic"
            ),
            instructions = "Cook noodles. Mix sauce. Serve cold.",
            isFavorite = false
        ),
        onRecipeClick = {},
        onToggleFavorite = {}
    )
}

@Preview
@Composable
fun RecipeCardPreviewEmpty() {
    RecipeCard(
        recipe = Recipe(
            id = 2,
            name = "Empty Recipe",
            imageUrl = null,
            category = RecipeCategory.BREAKFAST,
            cookTimeMinutes = 0,
            servings = 0,
            ingredients = emptyList(),
            instructions = "No instructions",
            isFavorite = false
        ),
        onRecipeClick = {},
        onToggleFavorite = {}
    )
}