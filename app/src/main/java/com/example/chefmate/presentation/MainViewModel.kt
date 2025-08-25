package com.example.chefmate.presentation

import HomeScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.chefmate.domain.model.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _backstack = mutableStateListOf<Screen>(Screen.Home)
    val backstack: List<Screen> get() = _backstack


    fun navigateTo(screen: Screen) {
        if (_backstack.lastOrNull() != screen) {
            _backstack.add(screen)
        }
    }


    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun popBack() {
        if (_backstack.size > 1) _backstack.removeLast()
    }
}