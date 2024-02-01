package com.example.playlistmaker.domain.favorites

import com.example.playlistmaker.db.domain.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class IsTrackAddedToFavoriteListUseCaseImpl(
    private val favoritesRepository: FavoritesRepository
) : IsTrackAddedToFavoriteListUseCase {
    override suspend fun invoke(trackId: Int): Flow<Boolean> {
        return favoritesRepository.isExist(trackId)
    }
}