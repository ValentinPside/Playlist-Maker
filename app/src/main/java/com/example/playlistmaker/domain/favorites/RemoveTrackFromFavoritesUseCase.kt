package com.example.playlistmaker.domain.favorites

import com.example.playlistmaker.search.domain.models.Track

interface RemoveTrackFromFavoritesUseCase {

    operator suspend fun invoke(track: Track)

}