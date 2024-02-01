package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.PlayList

interface CreatePlayListUseCase {

    operator suspend fun invoke(playList: PlayList): Long

}