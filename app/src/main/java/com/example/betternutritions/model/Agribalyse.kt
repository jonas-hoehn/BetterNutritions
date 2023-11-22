package com.example.betternutritions.model

data class Agribalyse(
    val agribalyse_food_code: String,
    val co2_agriculture: Int,
    val co2_consumption: Int,
    val co2_distribution: Int,
    val co2_packaging: Int,
    val co2_processing: Int,
    val co2_total: Int,
    val co2_transportation: Int,
    val code: String,
    val dqr: String,
    val ef_agriculture: Int,
    val ef_consumption: Int,
    val ef_distribution: Int,
    val ef_packaging: Int,
    val ef_processing: Int,
    val ef_total: Int,
    val ef_transportation: Int,
    val is_beverage: Int,
    val name_en: String,
    val score: Int,
    val version: String
)