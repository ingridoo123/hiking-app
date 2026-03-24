package com.example.aplikacje_mobline.presentation.trail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikacje_mobline.data.Trail
import com.example.aplikacje_mobline.data.TrailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrailDetailsViewModel @Inject constructor(private val repository: TrailRepository) : ViewModel() {

    private val _trail = MutableStateFlow<Trail?>(null)
    val trail: StateFlow<Trail?> = _trail

    fun loadTrail(id: Int) {
        viewModelScope.launch {
            _trail.value = repository.getById(id)
        }
    }


}