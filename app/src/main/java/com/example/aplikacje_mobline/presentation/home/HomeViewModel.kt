package com.example.aplikacje_mobline.presentation.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.aplikacje_mobline.data.Trail
import com.example.aplikacje_mobline.data.TrailRepository
import com.example.aplikacje_mobline.data.TrailType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(application: Application): AndroidViewModel(application)  {

    private val repository = TrailRepository(application)

    private val _selectedType = MutableStateFlow(TrailType.HIKING)
    val selectedType: StateFlow<TrailType> = _selectedType.asStateFlow()

    private val _filteredTrails = MutableStateFlow<List<Trail>>(emptyList())
    val filteredTrails: StateFlow<List<Trail>> = _filteredTrails.asStateFlow()

    init {
        loadTrails()
    }

    fun selectType(type: TrailType) {
        _selectedType.value = type
        loadTrails()
    }

    private fun loadTrails() {
        _filteredTrails.value = repository.getAll().filter { it.type == _selectedType.value }
    }

}