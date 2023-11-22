package com.example.betternutritions.model

data class EcoscoreData(
    val adjustments: Adjustments,
    val agribalyse: Agribalyse,
    val grade: String,
    val grades: Grades,
    val missing: Missing,
    val missing_data_warning: Int,
    val previous_data: PreviousData,
    val score: Int,
    val scores: Scores,
    val status: String
)