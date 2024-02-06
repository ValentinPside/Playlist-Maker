package com.example.playlistmaker.playlistDetails

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.DeletePlaylistUseCase
import com.example.playlistmaker.domain.playlist.DeleteTrackFromPlaylistUseCase
import com.example.playlistmaker.domain.playlist.GetPlayListByIdUseCase
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private val playlistId: Int,
    private val getPlayListUseCase: GetPlayListByIdUseCase,
    private val deleteTrackFromPlaylistUseCase: DeleteTrackFromPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase
) : ViewModel() {

    private val state = MutableStateFlow(State())
    fun observeUi() = state.asStateFlow()

    init {
        viewModelScope.launch {
            getPlayListUseCase(playlistId)
                .filterNotNull()
                .collect { playList ->
                state.update {
                    it.copy(
                        tracks = playList.tracks,
                        description = playList.description.orEmpty(),
                        title = playList.name,
                        count = playList.tracks.size,
                        duration = (playList.tracks.sumOf { it.trackTimeMillis ?: 0 } / (1000 * 60)).toInt(),
                        image = playList.uri
                    )
                }
            }
        }
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            deleteTrackFromPlaylistUseCase(playlistId = playlistId, trackId = track.trackId)
        }
    }

    fun deletePlayList() {
        viewModelScope.launch {
            deletePlaylistUseCase(playlistId)
            state.update { it.copy(isDeleted = true) }
        }
    }

}

data class State(
    val tracks: List<Track> = emptyList(),
    val count: Int = 0,
    val duration: Int = 0,
    val description: String = "",
    val title: String = "",
    val image: Uri? = null,
    val isDeleted: Boolean = false
)