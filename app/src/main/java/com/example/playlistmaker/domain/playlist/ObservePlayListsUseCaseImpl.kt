package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.domain.PlayList
import kotlinx.coroutines.flow.Flow

class ObservePlayListsUseCaseImpl(
    private val playListRepository: PlayListRepository,
) : ObservePlayListsUseCase {
    override suspend fun invoke(): Flow<List<PlayList>> {
        return playListRepository.observePlayListWithTracks()
    }
}