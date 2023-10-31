package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.AudioPlayerInteractor
import com.example.playlistmaker.search.domain.models.Track

class AudioPlayerViewModel (
    private val track: Track
) : ViewModel() {

    private val viewState = MutableLiveData(ViewState(track))

    fun observe(): LiveData<ViewState> = viewState
}

data class ViewState(
    val track: Track
)