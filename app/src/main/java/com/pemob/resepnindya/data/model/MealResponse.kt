package com.pemob.resepnindya.data.model

import com.google.gson.annotations.SerializedName

data class MealResponse(
    val meals: List<Meal>?
)

data class Meal(
    @SerializedName("idMeal") val idMeal: String,
    @SerializedName("strMeal") val strMeal: String?,
    @SerializedName("strMealThumb") val strMealThumb: String?,
    @SerializedName("strCategory") val strCategory: String?,
    @SerializedName("strArea") val strArea: String?,
    @SerializedName("strInstructions") val strInstructions: String?,
    @SerializedName("strYoutube") val strYoutube: String?,
    @SerializedName("strSource") val strSource: String?,
    
    // Ingredients
    @SerializedName("strIngredient1") val strIngredient1: String?,
    @SerializedName("strIngredient2") val strIngredient2: String?,
    @SerializedName("strIngredient3") val strIngredient3: String?,
    @SerializedName("strIngredient4") val strIngredient4: String?,
    @SerializedName("strIngredient5") val strIngredient5: String?,
    @SerializedName("strIngredient6") val strIngredient6: String?,
    @SerializedName("strIngredient7") val strIngredient7: String?,
    @SerializedName("strIngredient8") val strIngredient8: String?,
    @SerializedName("strIngredient9") val strIngredient9: String?,
    @SerializedName("strIngredient10") val strIngredient10: String?,
    
    // Measures
    @SerializedName("strMeasure1") val strMeasure1: String?,
    @SerializedName("strMeasure2") val strMeasure2: String?,
    @SerializedName("strMeasure3") val strMeasure3: String?,
    @SerializedName("strMeasure4") val strMeasure4: String?,
    @SerializedName("strMeasure5") val strMeasure5: String?,
    @SerializedName("strMeasure6") val strMeasure6: String?,
    @SerializedName("strMeasure7") val strMeasure7: String?,
    @SerializedName("strMeasure8") val strMeasure8: String?,
    @SerializedName("strMeasure9") val strMeasure9: String?,
    @SerializedName("strMeasure10") val strMeasure10: String?
) {
    // Extension function / property concept to group ingredients and measures
    val ingredientsWithMeasures: List<Pair<String, String>>
        get() = listOfNotNull(
            createIngredientPair(strIngredient1, strMeasure1),
            createIngredientPair(strIngredient2, strMeasure2),
            createIngredientPair(strIngredient3, strMeasure3),
            createIngredientPair(strIngredient4, strMeasure4),
            createIngredientPair(strIngredient5, strMeasure5),
            createIngredientPair(strIngredient6, strMeasure6),
            createIngredientPair(strIngredient7, strMeasure7),
            createIngredientPair(strIngredient8, strMeasure8),
            createIngredientPair(strIngredient9, strMeasure9),
            createIngredientPair(strIngredient10, strMeasure10)
        )

    private fun createIngredientPair(ingredient: String?, measure: String?): Pair<String, String>? {
        return if (!ingredient.isNullOrBlank()) {
            Pair(ingredient.trim(), (measure ?: "").trim())
        } else {
            null
        }
    }
}
