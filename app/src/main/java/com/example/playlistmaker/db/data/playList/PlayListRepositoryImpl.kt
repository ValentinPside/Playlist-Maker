package com.example.playlistmaker.db.data.playList

import android.util.Log
import com.example.playlistmaker.db.data.asEntity
import com.example.playlistmaker.db.data.dao.PlayListDao
import com.example.playlistmaker.db.data.dao.TrackDao
import com.example.playlistmaker.db.data.entity.TrackWithPlayList
import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.domain.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayListRepositoryImpl(
    private val dao: PlayListDao,
    private val tracksDao: TrackDao
): PlayListRepository {
    override suspend fun insert(playList: PlayList): Long {
        return dao.insert(playList.asRoom())
    }

    override suspend fun observeAll(): Flow<List<PlayList>> {
        return dao.observeAll().map { it.asDomain() }
    }

    override suspend fun observePlayListWithTracks(): Flow<List<PlayList>> {
        val flow = dao.observePlayListWithTracks()

        val result = flow.map {list ->
            list.mapNotNull {
                Log.d("TAG", "playlist: ${it?.playList}  tracks: ${it?.tracks}")
                it?.asDomain()
            }
        }

        return result

    }

    override suspend fun update(playList: PlayList) {
        dao.update(playList.asRoom())
    }

    override suspend fun delete(playListId: Int) {
        dao.deleteAllTracksFromPlaylist(playListId)
        dao.delete(playListId)
    }

    override suspend fun addTrackToPlaylist(track: Track, playListId: Int) {
        val item = TrackWithPlayList(
            playListId = playListId,
            trackId = track.trackId
        )
        dao.insertTrackWithPlayList(item)
        tracksDao.insertTrack(track.asEntity())
    }

    override suspend fun isTrackAdded(trackId: Int, playListId: Int): Boolean {
        return dao.isTrackAdded(trackId, playListId) != null
    }

    override suspend fun getPlayListById(playListId: Int): Flow<PlayList?> {
        return dao.getPlayListById(playListId).map { it.asDomain() }
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Int, trackId: Int) {
        dao.removeTrackFromPlaylist(playlistId = playlistId, trackId = trackId)
    }
}