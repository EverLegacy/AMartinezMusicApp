package com.example.amartinezmusicapp.data

import com.example.amartinezmusicapp.network.RetrofitClient

class MusicRepository {
    private val api = RetrofitClient.api

    suspend fun fetchAlbums() = runCatching { api.getAlbums() }
    suspend fun fetchAlbum(id: Int) = runCatching { api.getAlbum(id) }
}
