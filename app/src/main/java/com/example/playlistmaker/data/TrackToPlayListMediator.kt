package com.example.playlistmaker.data

import kotlinx.coroutines.flow.StateFlow

interface TrackToPlayListMediator {

    fun addTrack(trackId: Int?)

    fun observe(): StateFlow<Int?>

}