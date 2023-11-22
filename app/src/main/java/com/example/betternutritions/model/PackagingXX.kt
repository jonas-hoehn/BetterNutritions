package com.example.betternutritions.model

data class PackagingXX(
    val material: Material,
    val number_of_units: Int,
    val quantity_per_unit: String,
    val quantity_per_unit_unit: String,
    val quantity_per_unit_value: Int,
    val recycling: Recycling,
    val shape: Shape,
    val weight: Int,
    val weight_estimated: Int,
    val weight_measured: Int,
    val weight_source_id: String,
    val weight_specified: Int
)