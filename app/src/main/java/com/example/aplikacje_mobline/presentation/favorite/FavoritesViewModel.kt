package com.example.aplikacje_mobline.presentation.favorite

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
class FavoritesViewModel @Inject constructor(
    private val repository: TrailRepository
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Trail>>(emptyList())
    val favorites: StateFlow<List<Trail>> = _favorites.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _favorites.value = repository.getFavorites()
            } catch (exception: Exception) {
                _favorites.value = emptyList()
                _errorMessage.value = "Nie udalo sie pobrac ulubionych tras."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
