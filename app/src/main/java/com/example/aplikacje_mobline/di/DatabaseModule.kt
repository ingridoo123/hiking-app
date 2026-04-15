package com.example.aplikacje_mobline.di

import android.content.Context
import androidx.room.Room
import com.example.aplikacje_mobline.data.local.HikingDatabase
import com.example.aplikacje_mobline.data.local.TrailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHikingDatabase(@ApplicationContext context: Context): HikingDatabase =
        Room.databaseBuilder(context, HikingDatabase::class.java, "trails").build()

    @Provides
    fun provideTrailDao(hikingDatabase: HikingDatabase): TrailDao = hikingDatabase.trailDao()
}