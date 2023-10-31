package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.SearchTracksRepository

class ClearTracksHistoryUseCaseImpl(
    private val repository: SearchTracksRepository
) : ClearTracksHistoryUseCase {
    override fun invoke() {
        repository.clear()
    }
}