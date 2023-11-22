package com.example.betternutritions.model

data class ProductData(
    val code: String,
    val errors: List<Any>,
    val product: Product,
    val result: Result,
    val status: String,
    val warnings: List<Any>
)