package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.db.domain.PlayListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeletePlaylistUseCaseImpl(
    private val repository: PlayListRepository
) : DeletePlaylistUseCase {
    override suspend fun invoke(playlistId: Int) {
        withContext(Dispatchers.IO) {
            repository.delete(playlistId)
        }
    }
}