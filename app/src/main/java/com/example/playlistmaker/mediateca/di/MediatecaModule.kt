package com.example.playlistmaker.mediateca.di

import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.mediateca.ui.PlayListFragmentViewModel
import com.example.playlistmaker.mediateca.ui.new_playlist.NewPlaylistViewModel
import com.example.playlistmaker.mediateca.ui.tracks.TracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tracksViewModelModule = module {
    viewModel { TracksViewModel(get()) }
}
val playListFragmentViewModelModule = module {
    viewModel { PlayListFragmentViewModel(get<PlayListRepository>()) }
}

val newPlayListFragmentViewModelModule = module {
    viewModel { NewPlaylistViewModel(get<PlayListRepository>(), get()) }
}