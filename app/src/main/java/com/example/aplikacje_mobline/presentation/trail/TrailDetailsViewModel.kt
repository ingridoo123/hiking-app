package com.example.aplikacje_mobline.presentation.trail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikacje_mobline.data.Trail
import com.example.aplikacje_mobline.data.TrailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrailDetailsViewModel @Inject constructor(private val repository: TrailRepository) : ViewModel() {

    private val _trail = MutableStateFlow<Trail?>(null)
    val trail: StateFlow<Trail?> = _trail.asStateFlow()

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

}