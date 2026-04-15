package com.example.aplikacje_mobline.data

import com.example.aplikacje_mobline.data.local.TrailDao
import com.example.aplikacje_mobline.data.remote.TrailApi
import javax.inject.Inject

class TrailRepository @Inject constructor(
    private val api: TrailApi,
    private val dao: TrailDao
) {

    suspend fun getAll(): List<Trail> = try {
        val trails = api.getTrailsDetails()
        dao.insertAll(trails)
        dao.getAll()
    } catch (e: Exception) {
        dao.getAll()
    }

    suspend fun getById(id: Int): Trail? = api.getTrailsDetails().find { it.id == id }
}