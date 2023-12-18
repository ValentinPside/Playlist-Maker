package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.SearchTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchTracksUseCaseImpl(
    private val repository: SearchTracksRepository
) : SearchTracksUseCase {
    override suspend fun invoke(query: String, onError: () -> Unit, onSuccess: (List<Track>) -> Unit) {
        withContext(Dispatchers.IO) {
            repository.search(query, onSuccess, onError)
        }
    }
}