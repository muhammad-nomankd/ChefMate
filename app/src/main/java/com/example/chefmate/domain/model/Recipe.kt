package com.example.chefmate.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: RecipeCategory,
    val imageUrl: String?,
    val cookTimeMinutes: Int,
    val servings: Int,
    val ingredients: List<String>,
    val instructions: String,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()

    )
