package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface SearchTracksUseCase {

    suspend operator fun invoke(query: String, onError: () -> Unit, onSuccess: (List<Track>) -> Unit)

}