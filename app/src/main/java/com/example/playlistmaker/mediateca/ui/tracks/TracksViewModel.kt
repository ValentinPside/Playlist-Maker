package com.example.playlistmaker.mediateca.ui.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TracksViewModel(
    private val favoritesRepository: FavoritesRepository
): ViewModel() {

    private val state = MutableStateFlow(ViewState())
    fun uiState() = state.asStateFlow()

    init {
        viewModelScope.launch {
            favoritesRepository.getFromFavorites().collect{tracks ->
                state.update { it.copy(items = tracks) }
            }
        }
    }


}

data class ViewState(
    val items: List<Track> = emptyList()
)