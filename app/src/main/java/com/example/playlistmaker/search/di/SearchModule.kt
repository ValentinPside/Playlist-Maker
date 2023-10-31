package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.*
import com.example.playlistmaker.search.ui.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel {
        SearchViewModel(get(), get(), get(), get())
    }

    single<GetTracksHistoryUseCase> {
        GetTracksHistoryUseCaseImpl(get())
    }

    single<ClearTracksHistoryUseCase> {
        ClearTracksHistoryUseCaseImpl(get())
    }

    single<WriteTracksHistoryUseCase> {
        WriteTracksHistoryUseCaseImpl(get())
    }

    single<SearchTracksUseCase> {
        SearchTracksUseCaseImpl(get())
    }
}