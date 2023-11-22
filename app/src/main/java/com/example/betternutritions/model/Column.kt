package com.example.betternutritions.model

data class Column(
    val column_group_id: String,
    val shown_by_default: Boolean,
    val style: String,
    val text: String,
    val text_for_small_screens: String,
    val type: String
)