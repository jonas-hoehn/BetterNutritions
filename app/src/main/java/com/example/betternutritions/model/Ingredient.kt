package com.example.betternutritions.model

data class Ingredient(
    val from_palm_oil: String,
    val id: String,
    val ingredients: List<IngredientX>,
    val percent_estimate: Double,
    val percent_max: Double,
    val percent_min: Double,
    val text: String,
    val vegan: String,
    val vegetarian: String
)