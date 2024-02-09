package com.example.playlistmaker.domain.playlist

interface GetTracksByPlaylistUseCase {

    operator fun invoke(playlistId: Int)

}