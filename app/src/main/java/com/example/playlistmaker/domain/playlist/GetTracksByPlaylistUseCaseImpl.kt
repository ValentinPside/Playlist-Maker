package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.db.domain.PlayListRepository

class GetTracksByPlaylistUseCaseImpl(
    private val repository: PlayListRepository
) : GetTracksByPlaylistUseCase {
    override fun invoke(playlistId: Int) {
        TODO("Not yet implemented")
    }
}