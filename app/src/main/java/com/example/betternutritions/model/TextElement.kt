package com.example.betternutritions.model

data class TextElement(
    val edit_field_id: String,
    val edit_field_type: String,
    val edit_field_value: String,
    val html: String,
    val language: String,
    val lc: String,
    val source_language: String,
    val source_lc: String,
    val source_text: String,
    val source_url: String,
    val type: String
)