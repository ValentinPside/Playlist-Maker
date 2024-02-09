package com.example.playlistmaker.mediateca.ui.new_playlist

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.TrackToPlayListMediator
import com.example.playlistmaker.domain.PlayList
import com.example.playlistmaker.domain.playlist.AddTrackToPlayListUseCase
import com.example.playlistmaker.domain.playlist.CreatePlayListUseCase
import com.example.playlistmaker.domain.playlist.GetPlayListByIdUseCase
import com.example.playlistmaker.domain.playlist.UpdatePlaylistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistId: Int,
    private val trackToPlayListMediator: TrackToPlayListMediator,
    private val addTrackToPlayListUseCase: AddTrackToPlayListUseCase,
    private val createPlayListUseCase: CreatePlayListUseCase,
    private val getPlayListByIdUseCase: GetPlayListByIdUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase
) : ViewModel() {
    private val state = MutableStateFlow(ViewState())
    fun observeUi() = state.asStateFlow()

    private var playList: PlayList? = null
    private val id
        get() = playlistId.takeIf { it > -1 }


    init {
        id?.let {
            viewModelScope.launch {
                getPlayListByIdUseCase(it)
                    .filterNotNull()
                    .collect { playlist ->
                    playList = playlist
                    state.update {
                        it.copy(
                            name = playlist.name,
                            description = playlist.description,
                            image = playlist.uri
                        )
                    }
                }
            }
        }
    }

    fun onNameChanged(text: String) {
        state.update { it.copy(createEnabled = text.isNotEmpty(), name = text) }
    }

    fun setDescription(text: String?) {
        state.update { it.copy(description = text) }
    }

    fun setImage(uri: Uri?) {
        state.update { it.copy(image = uri) }
    }

    fun create(name: String, description: String) {
        if (id != null) {
            update(name, description)
            return
        }

        viewModelScope.launch {
            val playList = PlayList(
                id = 0,
                name = name,
                description = description,
                uri = state.value.image
            )
            val result = createPlayListUseCase(playList)

            trackToPlayListMediator.observe().value?.let {
                addTrackToPlayListUseCase(track = it, playListId = result.toInt())
                trackToPlayListMediator.addTrack(null)
            }
            state.update { it.copy(finish = true, name = name, description = description) }
        }
    }

    private fun update(name: String, description: String) {
        playList?.let {
            val model = it.copy(
                name = name,
                description = description,
                uri = state.value.image
            )

            viewModelScope.launch {
                updatePlaylistUseCase(model)
                state.update { it.copy(finish =  true, name = name, description = description) }
            }
        }
    }

}

data class ViewState(
    val createEnabled: Boolean = false,
    val name: String = "",
    val description: String? = null,
    val image: Uri? = null,
    val finish: Boolean = false
)