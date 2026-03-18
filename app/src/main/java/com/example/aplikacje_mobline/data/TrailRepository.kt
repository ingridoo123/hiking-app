package com.example.aplikacje_mobline.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrailRepository(private val context: Context) {

    private val trails: List<Trail> by lazy {
        val json = context.assets.open("trails.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Trail>>() {}.type
        Gson().fromJson(json, type)
    }

    fun getAll(): List<Trail> = trails

    fun getById(id: Int): Trail? = trails.find { it.id == id }
}