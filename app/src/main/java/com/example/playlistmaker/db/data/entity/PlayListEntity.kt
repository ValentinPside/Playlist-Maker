package com.example.playlistmaker.db.data.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val playListId: Int,
    val name: String,
    val description: String?,
    val uri: Uri?,
)
