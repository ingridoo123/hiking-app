package com.example.aplikacje_mobline.presentation.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikacje_mobline.data.Trail
import com.example.aplikacje_mobline.data.TrailRepository
import com.example.aplikacje_mobline.data.TrailType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: TrailRepository): ViewModel()  {

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
        viewModelScope.launch {
            val trails = repository.getAll().filter { it.type == _selectedType.value }
            _filteredTrails.value = trails
        }
    }

}