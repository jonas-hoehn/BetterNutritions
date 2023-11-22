package com.example.betternutritions.model

data class Ingredient(
    val id: String,
    val ingredients: String,
    val percent: Int,
    val percent_estimate: Int,
    val percent_max: Int,
    val percent_min: Int,
    val text: String,
    val vegan: String,
    val vegetarian: String
)