package com.pemob.resepnindya.data.repository

import com.pemob.resepnindya.data.api.RetrofitClient
import com.pemob.resepnindya.data.model.Meal

class RecipeRepository {
    private val api = RetrofitClient.apiService

    suspend fun searchRecipes(query: String): Result<List<Meal>> {
        return try {
            val response = api.searchRecipes(query)
            if (response.meals != null) {
                Result.success(response.meals)
            } else {
                Result.failure(Exception("Resep tidak ditemukan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipeDetails(id: String): Result<Meal> {
        return try {
            val response = api.getRecipeDetails(id)
            val meal = response.meals?.firstOrNull()
            if (meal != null) {
                Result.success(meal)
            } else {
                Result.failure(Exception("Detail resep tidak ditemukan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
