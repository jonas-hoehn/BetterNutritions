package com.example.betternutritions.model

data class IngredientX(
    val id: String,
    val ingredients: List<IngredientXX>,
    val percent_estimate: Double,
    val percent_max: Double,
    val percent_min: Int,
    val text: String,
    val vegan: String,
    val vegetarian: String
)