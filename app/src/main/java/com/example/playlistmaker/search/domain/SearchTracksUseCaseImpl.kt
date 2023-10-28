package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.SearchTracksRepository
import com.example.playlistmaker.search.domain.models.Track

class SearchTracksUseCaseImpl(
    private val repository: SearchTracksRepository
) : SearchTracksUseCase {
    override fun invoke(query: String, onError: () -> Unit, onSuccess: (List<Track>) -> Unit) {
        repository.search(query, onSuccess, onError)
    }
}