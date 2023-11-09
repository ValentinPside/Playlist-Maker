package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.ui.PlayListFragmentViewModel
import com.example.playlistmaker.sharing.ui.TracksFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tracksFragmentViewModelModule = module {
    viewModel { TracksFragmentViewModel() }
}
val playListFragmentViewModelModule = module {
    viewModel { PlayListFragmentViewModel() }
}