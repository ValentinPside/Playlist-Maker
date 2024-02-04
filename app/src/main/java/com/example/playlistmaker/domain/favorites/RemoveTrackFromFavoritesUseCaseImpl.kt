package com.example.playlistmaker.domain.favorites

import com.example.playlistmaker.db.domain.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track

class RemoveTrackFromFavoritesUseCaseImpl(
    private val favoritesRepository: FavoritesRepository,
) : RemoveTrackFromFavoritesUseCase {
    override suspend fun invoke(track: Track) {
        favoritesRepository.deleteFromFavorites(track)
    }
}