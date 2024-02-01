package com.example.playlistmaker.domain.playlist

interface AddTrackToPlayListUseCase {

    operator suspend fun invoke(trackId: Int, playListId: Int): Boolean

}