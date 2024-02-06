package com.example.playlistmaker.domain.playlist

interface DeletePlaylistUseCase {

    operator suspend fun invoke(playlistId: Int)

}