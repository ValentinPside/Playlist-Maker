package com.example.playlistmaker.domain.favorites

import kotlinx.coroutines.flow.Flow

interface IsTrackAddedToFavoriteListUseCase {

    operator suspend fun invoke(trackId: Int): Flow<Boolean>

}