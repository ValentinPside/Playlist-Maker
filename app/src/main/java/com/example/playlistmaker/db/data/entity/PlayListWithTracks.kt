package com.example.playlistmaker.db.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PlayListWithTracks(
    @Embedded val playList: PlayListEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "playListId"
    )
    val tracks: List<TrackWithPlayList>
)
