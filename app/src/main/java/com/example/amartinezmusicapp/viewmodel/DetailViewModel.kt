package com.example.amartinezmusicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amartinezmusicapp.data.Album
import com.example.amartinezmusicapp.data.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: MusicRepository = MusicRepository()
) : ViewModel() {

    private val _album = MutableStateFlow<UiState<Album>>(UiState.Idle)
    val album: StateFlow<UiState<Album>> = _album

    fun loadAlbum(id: String) {
        _album.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.fetchAlbum(id)
            _album.value = result.fold(
                onSuccess = { UiState.Success(data = it) },
                onFailure = { UiState.Error(message = it.message ?: "Unknown error") }
            )
        }
    }


}
