package com.example.playlistmaker

import java.util.*

data class Track(
    val trackName: String,
    val releaseDate: Date,
    val collectionName: String,
    val primaryGenreName: String,
    val country: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId : Int)