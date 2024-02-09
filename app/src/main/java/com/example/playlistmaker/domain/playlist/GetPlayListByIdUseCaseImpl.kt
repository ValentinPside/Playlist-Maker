package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.domain.PlayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetPlayListByIdUseCaseImpl(
    private val repository: PlayListRepository
) : GetPlayListByIdUseCase {
    override suspend fun invoke(playlistId: Int): Flow<PlayList?> {
        return withContext(Dispatchers.IO) {
            repository.getPlayListById(playlistId)
        }
    }
}