package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.db.domain.PlayListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddTrackToPlayListUseCaseImpl(
    private val playListRepository: PlayListRepository
) : AddTrackToPlayListUseCase {
    override suspend fun invoke(trackId: Int, playListId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val trackAdded = playListRepository.isTrackAdded(trackId, playListId)

            if (!trackAdded) {
                playListRepository.addTrackToPlaylist(trackId, playListId)
                true
            } else {
                false
            }
        }

    }
}