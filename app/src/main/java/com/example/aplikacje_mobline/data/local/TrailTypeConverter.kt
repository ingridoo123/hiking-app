package com.example.aplikacje_mobline.data.local

import androidx.room.TypeConverter
import com.example.aplikacje_mobline.data.TrailType

class TrailTypeConverter {

    @TypeConverter
    fun fromTrailType(value: TrailType): String = value.name

    @TypeConverter
    fun toTrailType(value: String): TrailType = TrailType.valueOf(value)
}