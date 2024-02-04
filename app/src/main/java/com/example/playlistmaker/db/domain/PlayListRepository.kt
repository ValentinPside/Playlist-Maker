package com.example.playlistmaker.db.domain

import com.example.playlistmaker.domain.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    suspend fun insert(playList: PlayList): Long

    suspend fun observeAll(): Flow<List<PlayList>>

    suspend fun observePlayListWithTracks(): Flow<List<PlayList>>

    suspend fun update(playList: PlayList)

    suspend fun delete(playList: PlayList)

    suspend fun addTrackToPlaylist(trackId: Int, playListId: Int)

    suspend fun isTrackAdded(trackId: Int, playListId: Int): Boolean

    suspend fun getPlayListById(playListId: Int): PlayList

}