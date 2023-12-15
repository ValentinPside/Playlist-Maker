package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface SearchTracksUseCase {

    operator suspend fun invoke(query: String, onError: () -> Unit, onSuccess: (List<Track>) -> Unit)

}