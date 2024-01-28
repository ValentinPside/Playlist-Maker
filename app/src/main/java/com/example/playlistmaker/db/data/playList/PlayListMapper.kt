package com.example.playlistmaker.db.data.playList

import com.example.playlistmaker.db.data.entity.PlayListEntity
import com.example.playlistmaker.db.data.entity.PlayListWithTracks
import com.example.playlistmaker.domain.PlayList

fun PlayListEntity.asDomain(): PlayList = PlayList(
    id = id,
    name = name,
    description = description,
    uri = uri
)

fun PlayList.asRoom(): PlayListEntity = PlayListEntity(
    id = id,
    name = name,
    description = description,
    uri = uri
)

fun List<PlayListEntity>.asDomain() = this.map { it.asDomain() }

fun PlayListWithTracks.asDomain(): PlayList = PlayList(
    id = this.playList.id,
    name = this.playList.name,
    description = this.playList.description,
    uri = this.playList.uri,
    tracks = this.tracks.map { it.trackId } ?: emptyList()
)