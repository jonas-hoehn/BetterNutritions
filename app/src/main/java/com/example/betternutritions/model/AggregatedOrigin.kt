package com.example.betternutritions.model

data class AggregatedOrigin(
    val epi_score: Int,
    val origin: String,
    val percent: Int,
    val transportation_score: Any
)