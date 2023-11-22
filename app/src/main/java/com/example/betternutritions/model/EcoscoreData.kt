package com.example.betternutritions.model

data class EcoscoreData(
    val adjustments: Adjustments,
    val agribalyse: Agribalyse,
    val missing: Missing,
    val missing_agribalyse_match_warning: Double,
    val scores: Scores,
    val status: String
)