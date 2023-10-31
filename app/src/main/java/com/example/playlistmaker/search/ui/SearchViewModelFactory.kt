package com.example.playlistmaker.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.search.domain.ClearTracksHistoryUseCase
import com.example.playlistmaker.search.domain.GetTracksHistoryUseCase
import com.example.playlistmaker.search.domain.SearchTracksUseCase
import com.example.playlistmaker.search.domain.WriteTracksHistoryUseCase

class SearchViewModelFactory (
    private val getTracksHistoryUseCase: GetTracksHistoryUseCase,
    private val clearTracksHistoryUseCase: ClearTracksHistoryUseCase,
    private val writeTracksHistoryUseCase: WriteTracksHistoryUseCase,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            getTracksHistoryUseCase,
            clearTracksHistoryUseCase,
            writeTracksHistoryUseCase,
            searchTracksUseCase
        ) as T
    }
}