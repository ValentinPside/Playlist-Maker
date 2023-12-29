package com.example.playlistmaker.db.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.playlistmaker.db.data.dao.TrackDao
import com.example.playlistmaker.db.data.entity.TrackEntity
import com.example.playlistmaker.db.domain.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoritesRepositoryImpl(
    private val dao: TrackDao,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoritesRepository {

    override suspend fun addToFavorites(track: Track) {
        withContext(Dispatchers.IO) {
            dao.insertTrack(trackDbConvertor.map(track))
        }
    }

    override suspend fun deleteFromFavorites(track: Track) {
        withContext(Dispatchers.IO) {
            dao.deleteTrack(trackDbConvertor.map(track))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getFromFavorites(): Flow<List<Track>> {
        return dao.getTracks().map { it.map { trackDbConvertor.map(it) } }
    }

    override suspend fun isExist(trackId: Int): Flow<Boolean> {
        return dao.trackIsExists(trackId)
    }

}