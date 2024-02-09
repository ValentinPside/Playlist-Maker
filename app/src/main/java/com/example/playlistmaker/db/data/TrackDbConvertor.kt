package com.example.playlistmaker.db.data

import com.example.playlistmaker.db.data.entity.TrackEntity
import com.example.playlistmaker.search.domain.models.Track

fun Track.asEntity(): TrackEntity = TrackEntity(
    trackId = trackId,
    artworkUrl100 = artworkUrl100,
    trackName = trackName,
    artistName = artistName,
    collectionName = collectionName,
    releaseDate = releaseDate,
    primaryGenreName = primaryGenreName,
    country = country,
    trackTimeMillis = trackTimeMillis,
    previewUrl = previewUrl,
    isFavorite = isFavorite
)

fun TrackEntity.asDomain(): Track = Track(
    trackName = trackName,
    releaseDate = releaseDate,
    collectionName = collectionName,
    primaryGenreName = primaryGenreName,
    country = country,
    artistName = artistName,
    trackTimeMillis = trackTimeMillis,
    artworkUrl100 = artworkUrl100,
    trackId = trackId,
    previewUrl = previewUrl,
    isFavorite = isFavorite
)