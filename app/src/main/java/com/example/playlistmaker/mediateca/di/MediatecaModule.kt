package com.example.playlistmaker.mediateca.di

import com.example.playlistmaker.domain.playlist.CreatePlayListUseCase
import com.example.playlistmaker.domain.playlist.CreatePlayListUseCaseImpl
import com.example.playlistmaker.domain.playlist.UpdatePlaylistUseCase
import com.example.playlistmaker.domain.playlist.UpdatePlaylistUseCaseImpl
import com.example.playlistmaker.mediateca.ui.PlayListViewModel
import com.example.playlistmaker.mediateca.ui.new_playlist.NewPlaylistViewModel
import com.example.playlistmaker.mediateca.ui.tracks.TracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tracksViewModelModule = module {
    viewModel { TracksViewModel(get()) }
}
val playListViewModelModule = module {
    viewModel { PlayListViewModel(get()) }
}

val newPlayListFragmentViewModelModule = module {

    single<CreatePlayListUseCase> {
        CreatePlayListUseCaseImpl(get())
    }

    single<UpdatePlaylistUseCase> {
        UpdatePlaylistUseCaseImpl(get())
    }

    viewModel { parameters -> NewPlaylistViewModel(parameters.get(), get(), get(), get(), get(), get()) }
}