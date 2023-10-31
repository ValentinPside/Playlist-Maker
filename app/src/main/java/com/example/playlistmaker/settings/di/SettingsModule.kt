package com.example.playlistmaker.settings.di

import com.example.playlistmaker.mapTheme.data.MapThemeRepositoryImpl
import com.example.playlistmaker.mapTheme.domain.*
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsViewModelModule = module {
    viewModel {
        SettingsViewModel(get(), get())
    }

    single<GetMapThemeUseCase> {
        GetMapThemeUseCaseImpl(get())
    }

    single<SetMapThemeUseCase> {
        SetMapThemeUseCaseImpl(get())
    }

    single<MapThemeRepository> {
        MapThemeRepositoryImpl(get())
    }
}