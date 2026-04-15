package com.example.aplikacje_mobline.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TrailType {
    HIKING,
    BIKING
}

@Entity(tableName = "trails")
data class Trail(
    @PrimaryKey val id: Int,
    val name: String,
    val difficulty: String,
    val distance: String,
    val description: String,
    val type: TrailType,
    val imagePath: String? = null
)

