package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddTrackToPlayListUseCaseImpl(
    private val playListRepository: PlayListRepository
) : AddTrackToPlayListUseCase {
    override suspend fun invoke(track: Track, playListId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val trackAdded = playListRepository.isTrackAdded(track.trackId, playListId)

            if (!trackAdded) {
                playListRepository.addTrackToPlaylist(track, playListId)
                true
            } else {
                false
            }
        }

    }
}