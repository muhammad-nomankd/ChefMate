package com.example.chefmate.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.chefmate.domain.model.Recipe
import com.example.chefmate.domain.model.RecipeCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("Select * From recipes ORDER BY createdAt DESC")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("Select * from recipes Where isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteRecipes():Flow<List<Recipe>>

    @Query("Select * from recipes Where category = :category Order by createdAt DESC")
    fun getRecipeByCategory(category: RecipeCategory): Flow<List<Recipe>>

    @Query("Select * from recipes Where name Like '%' || :query || '%' OR ingredients LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchRecipes(query: String): Flow<List<Recipe>>

    @Query("Select * from recipes Where id = :id")
    suspend fun getRecipeById(id:Long):Recipe?

    @Update
    suspend fun updateRecipe(recipe: Recipe)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe): Long

    @Query("UPDATE recipes SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Long)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Query("SELECT COUNT(*) FROM recipes")
    fun getTotalRecipesCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM recipes WHERE isFavorite = 1")
    fun getFavoriteRecipesCount(): Flow<Int>

}