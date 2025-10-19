package com.example.amartinezmusicapp.data

import com.squareup.moshi.Json

data class Album(
    @Json(name = "_id") val mongoId: String? = null,
    val id: String? = null,
    val title: String,
    val artist: String,
    val description: String,
    val image: String
) {
    val safeId: String
        get() = id ?: mongoId.orEmpty()
}
