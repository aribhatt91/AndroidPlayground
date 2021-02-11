package com.aribhatt.kotlinlearner.wally.data.entity

import java.util.*

data class Wallpaper(
    val id: String,
    val width: Int,
    val height: Int,
    val url: String,
    val thumb: String,
    val description: String,
    val created_at: Date
) {
}