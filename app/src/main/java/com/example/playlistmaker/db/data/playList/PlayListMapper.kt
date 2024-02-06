package com.example.playlistmaker.db.data.playList

import com.example.playlistmaker.db.data.asDomain
import com.example.playlistmaker.db.data.entity.PlayListEntity
import com.example.playlistmaker.db.data.entity.PlayListWithTracks
import com.example.playlistmaker.domain.PlayList

fun PlayListEntity.asDomain(): PlayList = PlayList(
    id = playListId,
    name = name,
    description = description,
    uri = uri
)

fun PlayList.asRoom(): PlayListEntity = PlayListEntity(
    playListId = id,
    name = name,
    description = description,
    uri = uri
)

fun List<PlayListEntity>.asDomain() = this.map { it.asDomain() }

fun PlayListWithTracks?.asDomain(): PlayList? {
    return this?.let {
        PlayList(
            id = this.playList.playListId,
            name = this.playList.name,
            description = this.playList.description,
            uri = this.playList.uri,
            tracks = this.tracks.map { it.asDomain() }
        )
    }
}
