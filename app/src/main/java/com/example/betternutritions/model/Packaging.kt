package com.example.betternutritions.model

data class Packaging(
    val non_recyclable_and_non_biodegradable_materials: Double,
    val packagings: List<PackagingX>,
    val score: Double,
    val value: Double
)