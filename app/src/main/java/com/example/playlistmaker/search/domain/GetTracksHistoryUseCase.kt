package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface GetTracksHistoryUseCase {

    suspend fun get(): List<Track>

}