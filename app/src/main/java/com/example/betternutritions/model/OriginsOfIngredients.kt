package com.example.betternutritions.model

data class OriginsOfIngredients(
    val aggregated_origins: List<AggregatedOrigin>,
    val epi_score: Int,
    val epi_value: Int,
    val origins_from_origins_field: List<String>,
    val transportation_scores: TransportationScores,
    val transportation_values: TransportationValues,
    val values: Values,
    val warning: String
)