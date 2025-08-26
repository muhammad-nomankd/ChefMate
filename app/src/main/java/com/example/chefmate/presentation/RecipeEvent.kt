package com.example.chefmate.presentation

sealed class RecipeEvent {
    data class showToast(val message: String): RecipeEvent()
}