package com.example.amartinezmusicapp.data

import java.io.Serializable

//api de los albums que mando
data class Album(
    val id: Int,
    val title: String,
    val artist: String,
    val image: String,
    val description: String? = null
) : Serializable
