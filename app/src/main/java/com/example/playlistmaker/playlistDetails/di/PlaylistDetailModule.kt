package com.example.playlistmaker.playlistDetails.di

import com.example.playlistmaker.domain.playlist.DeletePlaylistUseCase
import com.example.playlistmaker.domain.playlist.DeletePlaylistUseCaseImpl
import com.example.playlistmaker.domain.playlist.DeleteTrackFromPlaylistUseCase
import com.example.playlistmaker.domain.playlist.DeleteTrackFromPlaylistUseCaseImpl
import com.example.playlistmaker.domain.playlist.GetPlayListByIdUseCase
import com.example.playlistmaker.domain.playlist.GetPlayListByIdUseCaseImpl
import com.example.playlistmaker.playlistDetails.PlaylistDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val playlistDetailModule = module {

    single<GetPlayListByIdUseCase> {
        GetPlayListByIdUseCaseImpl(get())
    }

    single<DeleteTrackFromPlaylistUseCase> {
        DeleteTrackFromPlaylistUseCaseImpl(get())
    }

    single<DeletePlaylistUseCase> {
        DeletePlaylistUseCaseImpl(get())
    }
    viewModel { parameters -> PlaylistDetailViewModel(playlistId = parameters.get(), get(), get(), get()) }
}