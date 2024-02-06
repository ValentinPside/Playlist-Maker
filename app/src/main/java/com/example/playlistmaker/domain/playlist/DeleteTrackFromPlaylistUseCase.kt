package com.example.playlistmaker.domain.playlist

interface DeleteTrackFromPlaylistUseCase {

    operator suspend fun invoke(playlistId: Int, trackId: Int)

}