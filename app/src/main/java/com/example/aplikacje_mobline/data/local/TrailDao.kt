package com.example.aplikacje_mobline.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aplikacje_mobline.data.Trail


@Dao
interface TrailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trails: List<Trail>)

    @Query("SELECT * FROM trails")
    suspend fun getAll(): List<Trail>

    @Query("DELETE FROM trails WHERE id= :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM trails WHERE id= :id")
    suspend fun getById(id: Int): Trail?

    @Query("DELETE FROM trails")
    suspend fun clearAll()

    @Query("SELECT * FROM trails WHERE isFavourite = 1")
    suspend fun getFavorites(): List<Trail>

    @Query("UPDATE trails SET isFavourite=:isFavorite WHERE id=:id")
    suspend fun setFavorite(id: Int, isFavorite: Boolean)

    @Query("SELECT isFavourite FROM trails WHERE id=:id")
    suspend fun isFavorite(id: Int): Boolean


}