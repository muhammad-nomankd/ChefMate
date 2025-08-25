package com.example.chefmate.domain.usecase

import com.example.chefmate.domain.repository.RecipeRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(private val repository: RecipeRepository) {
    suspend operator fun invoke(id: Long) = repository.toggleFavorite(id)
}