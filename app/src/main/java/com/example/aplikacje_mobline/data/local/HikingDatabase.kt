package com.example.aplikacje_mobline.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.aplikacje_mobline.data.Trail

@Database(
    entities = [Trail::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TrailTypeConverter::class)
abstract class HikingDatabase: RoomDatabase() {
    abstract fun trailDao(): TrailDao
}
