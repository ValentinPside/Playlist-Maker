package com.example.playlistmaker.search.domain

import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.db.domain.FavoritesRepository
import com.example.playlistmaker.search.data.SearchTracksRepository
import com.example.playlistmaker.search.domain.models.Track

class GetTracksHistoryUseCaseImpl(
    private val historyRepository: SearchTracksRepository
) : GetTracksHistoryUseCase {

    override suspend fun get(): List<Track> {
        return historyRepository.historyTrackList
    }

}