package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.PlayList
import kotlinx.coroutines.flow.Flow

interface GetPlayListByIdUseCase {

    suspend operator fun invoke(playlistId: Int): Flow<PlayList?>

}