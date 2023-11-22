package com.example.betternutritions.model

data class IngredientXX(
    val id: String,
    val percent_estimate: Double,
    val percent_max: Double,
    val percent_min: Double,
    val text: String,
    val vegan: String,
    val vegetarian: String
)