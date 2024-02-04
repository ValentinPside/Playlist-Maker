package com.example.playlistmaker.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PlayListTracks")
data class TrackPlayList(
    @PrimaryKey
    val id: Int,
    val playListId: Int
)
