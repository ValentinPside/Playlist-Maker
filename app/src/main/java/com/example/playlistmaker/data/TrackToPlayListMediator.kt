package com.example.playlistmaker.data

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.StateFlow

interface TrackToPlayListMediator {

    fun addTrack(track: Track?)

    fun observe(): StateFlow<Track?>

}