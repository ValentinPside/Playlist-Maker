package com.example.playlistmaker.player.domain

import android.media.MediaPlayer

class GetMediaPlayerUseCase {
     fun execute() : MediaPlayer {
        return MediaPlayer()
    }
}