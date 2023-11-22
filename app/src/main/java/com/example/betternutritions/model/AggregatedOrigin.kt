package com.example.betternutritions.model

data class AggregatedOrigin(
    val epi_score: Double,
    val origin: String,
    val percent: Double,
    val transportation_score: Any
)