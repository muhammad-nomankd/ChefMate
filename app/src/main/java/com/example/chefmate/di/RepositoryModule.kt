package com.example.chefmate.di

import com.example.chefmate.data.repository.RecipeRepositoryImp
import com.example.chefmate.domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


}