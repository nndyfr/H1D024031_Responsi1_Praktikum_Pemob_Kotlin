package com.pemob.resepnindya.data.api

import com.pemob.resepnindya.data.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMealDbApi {
    @GET("search.php")
    suspend fun searchRecipes(@Query("s") query: String): MealResponse

    @GET("lookup.php")
    suspend fun getRecipeDetails(@Query("i") id: String): MealResponse
}
