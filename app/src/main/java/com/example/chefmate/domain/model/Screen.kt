package com.example.chefmate.domain.model

sealed interface Screen {
    object Home : Screen
    object Search : Screen
    object Add : Screen
    object Favorite : Screen
}