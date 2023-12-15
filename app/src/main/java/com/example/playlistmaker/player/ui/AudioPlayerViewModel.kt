package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val track: Track
) : ViewModel() {

    private val viewState = MutableStateFlow(ViewState(track))
    fun observe() = viewState.asStateFlow()

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var timerJob: Job? = null

    init {
        initMediaPlayer()
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
            viewState.update { it.copy(playerState = PlayerState.Prepared()) }
        }
        mediaPlayer.setOnCompletionListener {
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

    data class ViewState(
        val track: Track,
        val playerState: PlayerState = PlayerState.Default()
    )
}

