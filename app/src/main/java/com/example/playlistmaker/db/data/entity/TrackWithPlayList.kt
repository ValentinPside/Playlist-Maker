package com.example.playlistmaker.db.data.entity

import androidx.room.Entity

@Entity(tableName = "tracksWithPlayList", primaryKeys = ["playListId", "trackId"])
data class TrackWithPlayList (
    val playListId: Int,
    val trackId: Int
)