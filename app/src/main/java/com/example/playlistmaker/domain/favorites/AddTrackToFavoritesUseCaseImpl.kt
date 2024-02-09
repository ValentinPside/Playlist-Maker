package com.example.playlistmaker.domain.favorites

import com.example.playlistmaker.db.domain.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddTrackToFavoritesUseCaseImpl(
    private val favoritesRepository: FavoritesRepository,
) : AddTrackToFavoritesUseCase {
    override suspend fun invoke(track: Track) {
        withContext(Dispatchers.IO) {
            favoritesRepository.addToFavorites(track.copy(isFavorite = true))
        }
    }
}