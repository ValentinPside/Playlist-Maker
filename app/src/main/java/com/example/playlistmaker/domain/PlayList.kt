package com.example.playlistmaker.domain

import android.net.Uri

data class PlayList(
    val id: Int,
    val name: String,
    val description: String?,
    val uri: Uri?,
    val tracks: List<Int> = emptyList()
)
