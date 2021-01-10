package com.aribhatt.kotlinlearner.data.entities

data class Monster (
    val name: String,
    val imageFile: String,
    val caption: String,
    val description: String,
    val price: Double,
    val scariness: Int
)