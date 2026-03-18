package com.example.aplikacje_mobline.presentation.trail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.aplikacje_mobline.data.Trail
import com.example.aplikacje_mobline.data.TrailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TrailDetailsViewModel(application: Application): AndroidViewModel(application) {

    private val repository = TrailRepository(application)

    private val _trail = MutableStateFlow<Trail?>(null)
    val trail: StateFlow<Trail?> = _trail

    fun loadTrail(id: Int) {
        _trail.value = repository.getById(id)
    }


}