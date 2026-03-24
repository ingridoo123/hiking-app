package com.example.aplikacje_mobline.data

import com.example.aplikacje_mobline.data.remote.TrailApi
import javax.inject.Inject

class TrailRepository @Inject constructor(private val api: TrailApi) {

    suspend fun getAll(): List<Trail> = api.getTrailsDetails()

    suspend fun getById(id: Int): Trail? = api.getTrailsDetails().find { it.id == id }
}