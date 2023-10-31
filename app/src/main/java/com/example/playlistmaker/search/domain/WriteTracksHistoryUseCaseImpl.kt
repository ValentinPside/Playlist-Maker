package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.SearchTracksRepository
import com.example.playlistmaker.search.domain.models.Track

class WriteTracksHistoryUseCaseImpl(
    private  val repository: SearchTracksRepository
) : WriteTracksHistoryUseCase {
    override fun write(searchHistoryTrackList: List<Track>, track: Track) {
        repository.write(searchHistoryTrackList, track)
    }
}