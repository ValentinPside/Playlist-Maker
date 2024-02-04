package com.example.playlistmaker.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TrackToPlayListMediatorImpl : TrackToPlayListMediator {

    private val state = MutableStateFlow<Int?>(null)

    override fun addTrack(trackId: Int?) {
        state.update { trackId }
    }

    override fun observe(): StateFlow<Int?> = state.asStateFlow()
}