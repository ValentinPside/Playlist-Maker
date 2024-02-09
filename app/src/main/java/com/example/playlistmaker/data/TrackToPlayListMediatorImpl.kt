package com.example.playlistmaker.data

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TrackToPlayListMediatorImpl : TrackToPlayListMediator {

    private val state = MutableStateFlow<Track?>(null)

    override fun addTrack(track: Track?) {
        state.update { track }
    }

    override fun observe(): StateFlow<Track?> = state.asStateFlow()
}