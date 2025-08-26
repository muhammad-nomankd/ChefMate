
https://github.com/user-attachments/assets/bce50f87-65f5-4fe1-be6e-0ef48979e54b


1. Project Title & Description

Name of app (ChefMate)

Short description: “ChefMate is a recipe management app built with Jetpack Compose. It allows users to add, search, and favorite recipes with a clean UI and offline storage.”

2. Features

Add recipes with title, ingredients, and instructions

Save recipes locally (Room database)

Search and filter recipes

Mark/unmark recipes as favorites

Modern Compose UI with bottom navigation

3. Tech Stack

Kotlin – main language

Jetpack Compose – UI toolkit

Hilt – dependency injection

Room Database – local storage

MVVM + Clean Architecture – scalable code structure

Repository Pattern – separation of concerns

Navigation 3 – for screen management

4. App Architecture (Data Flow)

UI → ViewModel → UseCase → Repository → Room DB → back to UI

State handled with StateFlow / UiState

5. Setup & Installation

Clone repo:

git clone https://github.com/yourusername/ChefMate.git


Open in Android Studio (latest version)

Build & run on emulator or device (API 26+)
