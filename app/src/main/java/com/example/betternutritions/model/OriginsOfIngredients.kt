package com.example.betternutritions.model

data class OriginsOfIngredients(
    val aggregated_origins: List<AggregatedOrigin>,
    val epi_score: Double,
    val epi_value: Double,
    val origins_from_origins_field: List<String>,
    val transportation_score: Double,
    val transportation_scores: TransportationScores,
    val transportation_value: Double,
    val transportation_values: TransportationValues,
    val value: Double,
    val values: Values,
    val warning: String
)