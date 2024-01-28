package com.example.playlistmaker.db.data.playList

import com.example.playlistmaker.db.data.dao.PlayListDao
import com.example.playlistmaker.db.data.entity.TrackWithPlayList
import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.domain.PlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayListRepositoryImpl(
    private val dao: PlayListDao
): PlayListRepository {
    override suspend fun insert(playList: PlayList): Long {
        return dao.insert(playList.asRoom())
    }

    override suspend fun observeAll(): Flow<List<PlayList>> {
        return dao.observeAll().map { it.asDomain() }
    }

    override suspend fun observePlayListWithTracks(): Flow<List<PlayList>> {
        return dao.observePlayListWithTracks()
            .map {
                it.map { it.asDomain() }
            }
    }

    override suspend fun update(playList: PlayList) {
        dao.update(playList.asRoom())
    }

    override suspend fun delete(playList: PlayList) {
        dao.delete(playList.asRoom())
    }

    override suspend fun addTrackToPlaylist(trackId: Int, playListId: Int) {
        val item = TrackWithPlayList(
            playListId = playListId,
            trackId = trackId
        )
        dao.insertTrackWithPlayList(item)
    }

    override suspend fun isTrackAdded(trackId: Int, playListId: Int): Boolean {
        return dao.isTrackAdded(trackId, playListId) != null
    }

    override suspend fun getPlayListById(playListId: Int): PlayList {
        return dao.getPlayListById(playListId).asDomain()
    }
}