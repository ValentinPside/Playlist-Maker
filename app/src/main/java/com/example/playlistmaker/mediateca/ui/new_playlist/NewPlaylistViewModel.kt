package com.example.playlistmaker.mediateca.ui.new_playlist

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.TrackToPlayListMediator
import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.domain.PlayList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val repository: PlayListRepository,
    private val trackToPlayListMediator: TrackToPlayListMediator
) : ViewModel() {
    private val state = MutableStateFlow(ViewState())
    fun observeUi() = state.asStateFlow()

    fun onNameChanged(text: String) {
        state.update { it.copy(createEnabled = text.isNotEmpty(), name = text) }
    }

    fun setDescription(text: String?) {
        state.update { it.copy(description = text) }
    }

    fun setImage(uri: Uri?) {
        state.update { it.copy(image = uri) }
    }

    fun create() {
        viewModelScope.launch {
            val playList = PlayList(
                id = 0,
                name = state.value.name,
                description = state.value.description,
                uri = state.value.image
            )
            val result = repository.insert(playList)
            trackToPlayListMediator.observe().value?.let {
                repository.addTrackToPlaylist(it, result.toInt())
                trackToPlayListMediator.addTrack(null)
            }
            state.update { it.copy(finish = true) }
        }
    }

    fun onBack() {
        trackToPlayListMediator.addTrack(null)
    }

}

data class ViewState(
    val createEnabled: Boolean = false,
    val name: String = "",
    val description: String? = null,
    val image: Uri? = null,
    val finish: Boolean = false
)