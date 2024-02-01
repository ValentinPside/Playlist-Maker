package com.example.playlistmaker.player.ui

import android.content.Context
import android.media.MediaPlayer
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.data.TrackToPlayListMediator
import com.example.playlistmaker.domain.PlayList
import com.example.playlistmaker.domain.favorites.AddTrackToFavoritesUseCase
import com.example.playlistmaker.domain.favorites.IsTrackAddedToFavoriteListUseCase
import com.example.playlistmaker.domain.favorites.RemoveTrackFromFavoritesUseCase
import com.example.playlistmaker.domain.playlist.AddTrackToPlayListUseCase
import com.example.playlistmaker.domain.playlist.ObservePlayListsUseCase
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val context: Context,
    private val track: Track,
    private val trackToPlayListMediator: TrackToPlayListMediator,
    private val isTrackAddedToFavoriteListUseCase: IsTrackAddedToFavoriteListUseCase,
    private val addTrackToFavoritesUseCase: AddTrackToFavoritesUseCase,
    private val removeTrackFromFavoritesUseCase: RemoveTrackFromFavoritesUseCase,
    private val observePlayListsUseCase: ObservePlayListsUseCase,
    private val addTrackToPlayListUseCase: AddTrackToPlayListUseCase
) : ViewModel() {

    private val viewState = MutableStateFlow(ViewState(track))
    fun observe() = viewState.asStateFlow()

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var timerJob: Job? = null

    init {
        initMediaPlayer()
        viewModelScope.launch {
            isTrackAddedToFavoriteListUseCase(track.trackId).collect {isFavorite ->
                viewState.update { it.copy(isFavorite = isFavorite) }
            }
        }

        viewModelScope.launch {
            observePlayListsUseCase().collect{ items ->
                viewState.update { it.copy(playLists = items) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun onPause() {
        pausePlayer()
    }

    fun onPlayButtonClicked() {
        when (viewState.value.playerState) {
            is PlayerState.Playing -> {
                pausePlayer()
            }

            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }

            else -> {}
        }
    }

    private fun initMediaPlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            timerJob?.cancel()
            viewState.update { it.copy(playerState = PlayerState.Prepared()) }
        }
        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            viewState.update { it.copy(playerState = PlayerState.Prepared()) }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        viewState.update { it.copy(playerState = PlayerState.Playing(getCurrentPlayerPosition())) }
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        viewState.update { it.copy(playerState = PlayerState.Paused(getCurrentPlayerPosition())) }
    }

    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        viewState.update { it.copy(playerState = PlayerState.Default()) }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(300L)
                viewState.update { it.copy(playerState = PlayerState.Playing(getCurrentPlayerPosition())) }
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition) ?: "00:00"
    }

    fun onFavoriteClick() {
        viewModelScope.launch {
            if (!viewState.value.isFavorite){
                addTrackToFavoritesUseCase(track)
            } else {
                removeTrackFromFavoritesUseCase(track)
            }
        }
    }

    fun addTrackToPlayList(playList: PlayList) {
        viewModelScope.launch {
            val result = addTrackToPlayListUseCase(track.trackId, playList.id)
            if (result) {
                val text = ContextCompat.getString(context, R.string.track_is_added_to_playlist).format(playList.name)
                viewState.update { it.copy(error = text) }
            } else {
                val text = ContextCompat.getString(context, R.string.track_is_added).format(playList.name)
                viewState.update { it.copy(error = text) }
            }

        }
    }

    fun clearError() {
        viewState.update { it.copy(error = null) }
    }

    fun createNewPlayList() {
        trackToPlayListMediator.addTrack(track.trackId)
    }

}

data class ViewState(
    val track: Track,
    val playerState: PlayerState = PlayerState.Default(),
    val isFavorite: Boolean = false,
    val playLists: List<PlayList> = emptyList(),
    val error: String? = null
)


