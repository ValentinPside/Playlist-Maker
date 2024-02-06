package com.example.playlistmaker.db.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlayListWithTracks(
    @Embedded val playList: PlayListEntity,
    @Relation(
        parentColumn = "playListId",
        entityColumn = "trackId",
        associateBy = Junction(TrackWithPlayList::class)
    )
    val tracks: List<TrackEntity>
)
