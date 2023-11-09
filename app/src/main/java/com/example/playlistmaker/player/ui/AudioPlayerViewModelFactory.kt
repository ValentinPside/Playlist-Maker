package com.example.playlistmaker.player.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.search.domain.models.Track

class AudioPlayerViewModelFactory(
    private val track: Track
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AudioPlayerViewModel(track) as T
    }
}