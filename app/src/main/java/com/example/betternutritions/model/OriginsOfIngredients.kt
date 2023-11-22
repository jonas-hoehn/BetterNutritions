package com.example.betternutritions.model

data class OriginsOfIngredients(
    val aggregated_origins: List<AggregatedOrigin>,
    val epi_score: Int,
    val epi_value: Int,
    val origins_from_origins_field: List<String>,
    val transportation_score: Int,
    val transportation_scores: TransportationScores,
    val transportation_value: Int,
    val transportation_values: TransportationValues,
    val value: Int,
    val values: Values,
    val warning: String
)