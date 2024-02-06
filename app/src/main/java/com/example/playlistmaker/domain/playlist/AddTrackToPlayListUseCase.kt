package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.search.domain.models.Track

interface AddTrackToPlayListUseCase {

    operator suspend fun invoke(track: Track, playListId: Int): Boolean

}