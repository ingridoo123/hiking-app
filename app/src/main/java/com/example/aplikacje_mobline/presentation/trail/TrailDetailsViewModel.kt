package com.example.aplikacje_mobline.presentation.trail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikacje_mobline.data.Trail
import com.example.aplikacje_mobline.data.TrailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrailDetailsViewModel @Inject constructor(private val repository: TrailRepository) : ViewModel() {

    private val _trail = MutableStateFlow<Trail?>(null)
    val trail: StateFlow<Trail?> = _trail.asStateFlow()
    private val _isFavorite = MutableStateFlow<Boolean>(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadTrail(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _trail.value = repository.getById(id)
                if (_trail.value == null) {
                    _errorMessage.value = "Nie znaleziono szlaku."
                }
                _isFavorite.value = _trail.value?.isFavourite == true
            } catch (exception: Exception) {
                _trail.value = null
                _errorMessage.value = "Nie udalo sie pobrac szczegolow szlaku."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retry(id: Int) {
        loadTrail(id)
    }

    fun toggleFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            val trailId = trail.value?.id
            try {
                trailId?.let { repository.toggleFavorite(it) }
                val updatedTrail = trailId?.let { repository.getById(it) }
                _trail.value = updatedTrail
                _isFavorite.value = updatedTrail?.isFavourite == true
            } catch (e: Exception) {
                _errorMessage.value = "Nie udalo sie zaktualizowac ulubionych."
            }
        }


    }

}