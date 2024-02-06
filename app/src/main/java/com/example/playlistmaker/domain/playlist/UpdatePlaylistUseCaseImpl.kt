package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.domain.PlayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdatePlaylistUseCaseImpl(
    private val repository: PlayListRepository
) : UpdatePlaylistUseCase {
    override suspend fun invoke(playlist: PlayList) {
        withContext(Dispatchers.IO) {
            repository.update(playlist)
        }
    }
}