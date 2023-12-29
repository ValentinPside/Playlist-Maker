package com.example.playlistmaker.db.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun addToFavorites(track: Track)

    suspend fun deleteFromFavorites(track: Track)

    suspend fun getFromFavorites() : Flow<List<Track>>

    suspend fun isExist(trackId: Int): Flow<Boolean>
}