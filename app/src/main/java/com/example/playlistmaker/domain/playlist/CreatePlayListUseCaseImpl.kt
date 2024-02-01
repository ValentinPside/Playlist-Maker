package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.domain.PlayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreatePlayListUseCaseImpl(
    private val repository: PlayListRepository
) : CreatePlayListUseCase {
    override suspend fun invoke(playList: PlayList): Long {
        return withContext(Dispatchers.IO) {
            repository.insert(playList)
        }
    }
}