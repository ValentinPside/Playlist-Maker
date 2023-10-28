package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.remote.SearchHistoryRemoteDataSource
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface SearchTracksRepository {

    val historyTrackList: List<Track>

    fun write(searchHistoryTrackList : List<Track>, track: Track)

    fun clear()

    fun search(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit)

}

class SearchTracksRepositoryImpl(
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

    override fun search(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit) {
        val result = remoteDataStore.search(query)

        val callback = object: Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.code() == 200) {
                    val tracks = response.body()?.results
                    tracks?.let(onSuccess) ?: onError.invoke()
                } else {
                    onError.invoke()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                onError.invoke()
            }

        }

        result.enqueue(callback)
    }


    companion object {
        const val SEARCH_HISTORY_TRACK_KEY = "search_history_track_key"
    }

}