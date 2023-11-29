package com.example.playlistmaker.player.di


import android.media.MediaPlayer
import com.example.playlistmaker.player.ui.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {
    viewModel {(track: Track) ->
        AudioPlayerViewModel(track)
    }

    single<MediaPlayer> {
        MediaPlayer()
    }
}