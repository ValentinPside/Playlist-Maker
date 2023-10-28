package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface WriteTracksHistoryUseCase {

    fun write(searchHistoryTrackList : List<Track>, track: Track)

}