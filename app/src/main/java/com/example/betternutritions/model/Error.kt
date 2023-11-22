package com.example.betternutritions.model

data class Error(
    val `field`: Field,
    val impact: Impact,
    val message: Message
)