package com.example.aplikacje_mobline.data

enum class TrailType {
    HIKING,
    BIKING
}

data class Trail(
    val id: Int,
    val name: String,
    val difficulty: String,
    val distance: String,
    val description: String,
    val type: TrailType,
    val imagePath: String? = null
)

