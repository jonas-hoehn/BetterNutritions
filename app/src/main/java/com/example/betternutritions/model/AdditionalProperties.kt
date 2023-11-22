package com.example.betternutritions.model

data class AdditionalProperties(
    val elements: List<Element>,
    val expand_for: String,
    val expanded: Boolean,
    val level: String,
    val size: String,
    val title_element: TitleElement,
    val topics: List<String>,
    val type: String
)