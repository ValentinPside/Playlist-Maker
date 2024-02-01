package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.PlayList
import kotlinx.coroutines.flow.Flow

interface ObservePlayListsUseCase {

    operator suspend fun invoke(): Flow<List<PlayList>>

}