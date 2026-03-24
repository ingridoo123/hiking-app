package com.example.aplikacje_mobline.data.remote

import com.example.aplikacje_mobline.data.Trail
import retrofit2.http.GET
import retrofit2.http.Query

interface TrailApi {

    @GET("trails.json")
    suspend fun getTrailsDetails(): List<Trail>


}