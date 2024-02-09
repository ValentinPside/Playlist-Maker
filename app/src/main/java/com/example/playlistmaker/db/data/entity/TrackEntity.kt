package com.example.playlistmaker.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "track_table")
data class TrackEntity (
    @PrimaryKey
    val trackId: Int,
    val artworkUrl100: String?,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate: Date?,
    val primaryGenreName: String?,
    val country: String?,
    val trackTimeMillis: Long?,
    val previewUrl : String?,
    val dateAdded: Date = Date(),
    val isFavorite: Boolean
)