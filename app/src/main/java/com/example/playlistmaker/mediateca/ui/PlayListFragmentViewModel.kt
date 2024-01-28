package com.example.playlistmaker.mediateca.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.domain.PlayList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayListFragmentViewModel(
    private val playListRepository: PlayListRepository
): ViewModel() {

    private val state = MutableStateFlow(ViewState())
    fun observeUi() = state.asStateFlow()

    init {
        viewModelScope.launch {
            playListRepository.observePlayListWithTracks().collect{ items ->
                state.update { it.copy(playListItems = items) }
            }
        }
    }

}

data class ViewState(
    val playListItems: List<PlayList> = emptyList()
)