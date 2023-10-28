package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface GetTracksHistoryUseCase {

    fun get(): List<Track>

}