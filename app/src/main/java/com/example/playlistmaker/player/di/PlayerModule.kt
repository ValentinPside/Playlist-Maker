package com.example.playlistmaker.player.di


import android.media.MediaPlayer
import com.example.playlistmaker.domain.favorites.AddTrackToFavoritesUseCase
import com.example.playlistmaker.domain.favorites.AddTrackToFavoritesUseCaseImpl
import com.example.playlistmaker.domain.favorites.IsTrackAddedToFavoriteListUseCase
import com.example.playlistmaker.domain.favorites.IsTrackAddedToFavoriteListUseCaseImpl
import com.example.playlistmaker.domain.favorites.RemoveTrackFromFavoritesUseCase
import com.example.playlistmaker.domain.favorites.RemoveTrackFromFavoritesUseCaseImpl
import com.example.playlistmaker.domain.playlist.AddTrackToPlayListUseCase
import com.example.playlistmaker.domain.playlist.AddTrackToPlayListUseCaseImpl
import com.example.playlistmaker.domain.playlist.ObservePlayListsUseCase
import com.example.playlistmaker.domain.playlist.ObservePlayListsUseCaseImpl
import com.example.playlistmaker.player.ui.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {
    viewModel {(track: Track) ->
        AudioPlayerViewModel(get(), track, get(), get(), get(), get(), get(), get())
    }

    single<MediaPlayer> {
        MediaPlayer()
    }

    single<IsTrackAddedToFavoriteListUseCase> {
        IsTrackAddedToFavoriteListUseCaseImpl(get())
    }

    single<AddTrackToFavoritesUseCase> {
        AddTrackToFavoritesUseCaseImpl(get())
    }

    single<RemoveTrackFromFavoritesUseCase> {
        RemoveTrackFromFavoritesUseCaseImpl(get())
    }

    single<ObservePlayListsUseCase> {
        ObservePlayListsUseCaseImpl(get())
    }

    single<AddTrackToPlayListUseCase> {
        AddTrackToPlayListUseCaseImpl(get())
    }
}