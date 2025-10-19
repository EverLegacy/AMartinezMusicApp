package com.example.amartinezmusicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amartinezmusicapp.data.Album
import com.example.amartinezmusicapp.data.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MusicRepository = MusicRepository()
) : ViewModel() {

    private val _albums = MutableStateFlow<UiState<List<Album>>>(UiState.Idle)
    val albums: StateFlow<UiState<List<Album>>> = _albums

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        _albums.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.fetchAlbums()
            _albums.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}
