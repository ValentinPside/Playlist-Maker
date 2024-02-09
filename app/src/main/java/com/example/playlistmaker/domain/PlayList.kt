package com.example.playlistmaker.domain

import android.net.Uri
import com.example.playlistmaker.search.domain.models.Track

data class PlayList(
    val id: Int,
    val name: String,
    val description: String?,
    val uri: Uri?,
    val tracks: List<Track> = emptyList()
)
