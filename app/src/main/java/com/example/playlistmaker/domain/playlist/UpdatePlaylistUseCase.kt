package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.PlayList

interface UpdatePlaylistUseCase {

    operator suspend fun invoke(playlist: PlayList)

}