package com.example.betternutritions.model

data class PackagingX(
    val ecoscore_material_score: Int,
    val ecoscore_shape_ratio: Double,
    val material: String,
    val non_recyclable_and_non_biodegradable: String,
    val shape: String
)