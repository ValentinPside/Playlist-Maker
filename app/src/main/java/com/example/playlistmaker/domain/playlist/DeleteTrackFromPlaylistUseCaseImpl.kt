package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.db.domain.PlayListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteTrackFromPlaylistUseCaseImpl(
    private val repository: PlayListRepository
) : DeleteTrackFromPlaylistUseCase {
    override suspend fun invoke(playlistId: Int, trackId: Int) {
        withContext(Dispatchers.IO) {
            repository.removeTrackFromPlaylist(playlistId = playlistId, trackId = trackId)
        }
    }
}