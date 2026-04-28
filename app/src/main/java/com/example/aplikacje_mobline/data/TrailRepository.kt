package com.example.aplikacje_mobline.data

import com.example.aplikacje_mobline.data.local.TrailDao
import com.example.aplikacje_mobline.data.remote.TrailApi
import javax.inject.Inject

class TrailRepository @Inject constructor(
    private val api: TrailApi,
    private val dao: TrailDao
) {

    suspend fun getAll(): List<Trail> = try {
        val remoteTrails = api.getTrailsDetails()
        val localTails = dao.getAll().associateBy { it.id }
        val mergedTrails = remoteTrails.map { trail ->
            val trails = localTails[trail.id]
            trail.copy(
                isFavourite = trails?.isFavourite ?: false
            )
        }
        dao.insertAll(mergedTrails)
        dao.getAll()
    } catch (e: Exception) {
        dao.getAll()
    }

    suspend fun getById(id: Int): Trail? {
        val local = dao.getById(id)

        if (local != null) return local

        return try {
            val remote = api.getTrailsDetails().find { it.id == id } ?: return null
            dao.insertAll(listOf(remote))
            dao.getById(id)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getFavorites(): List<Trail> = dao.getFavorites()

    suspend fun toggleFavorite(id: Int) {
        val trail = dao.getById(id) ?: return
        dao.setFavorite(id, !trail.isFavourite)
    }

    suspend fun setFavorite(id: Int, isFavorite: Boolean) {
        dao.setFavorite(id, isFavorite)
    }
}