package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.search.data.remote.SearchHistoryRemoteDataSource
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

interface SearchTracksRepository {

    val historyTrackList: List<Track>

    fun write(searchHistoryTrackList : List<Track>, track: Track)

    fun clear()

    suspend fun search(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit)

}

class SearchTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val sharedPreferences: SharedPreferences,
    private val remoteDataStore: SearchHistoryRemoteDataSource
): SearchTracksRepository {

    override val historyTrackList: List<Track>
        get() = read()

    private var trackCount: Int = historyTrackList.size

    // чтение
    private fun read(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_TRACK_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    // запись
     override fun write(searchHistoryTrackList: List<Track>, track: Track) {
        val trackHistory = searchHistoryTrackList.toMutableList()

        when {
            trackCount < 10 && !trackHistory.contains(track) -> {trackHistory.add(0, track)
                trackCount += 1}
            trackCount == 10 && !trackHistory.contains(track) -> {trackHistory.add(0, track)
                trackHistory.removeAt(9)}
            else -> {trackHistory.remove(track)
                trackHistory.add(0, track)}
        }
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_TRACK_KEY, Gson().toJson(trackHistory))
            .apply()
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    override suspend fun search(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit) {
        remoteDataStore.search(query)
            .flowOn(Dispatchers.IO)
            .catch {
                Log.d("TAG", "$it")
                onError.invoke()
            }
            .collect { onSuccess(it.results) }
    }


    companion object {
        const val SEARCH_HISTORY_TRACK_KEY = "search_history_track_key"
    }

}