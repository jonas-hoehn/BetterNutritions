package com.example.betternutritions.model

data class Source(
    val fields: List<String>,
    val id: String,
    val images: List<Image>,
    val import_t: Int,
    val manufacturer: Int,
    val name: String,
    val source_licence: String,
    val source_licence_url: String,
    val url: Any
)