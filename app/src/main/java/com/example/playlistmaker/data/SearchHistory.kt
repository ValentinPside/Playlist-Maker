package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.SEARCH_HISTORY_TRACK_KEY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(sharedPreferences : SharedPreferences) {

    val historyTrackList : List<Track> = read(sharedPreferences)
    private var trackCount : Int = historyTrackList.size

    // чтение
    private fun read(sharedPreferences : SharedPreferences): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_TRACK_KEY, null) ?: return emptyList<Track>()
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    // запись
     fun write(sharedPreferences : SharedPreferences, searchHistoryTrackList : ArrayList<Track>, track: Track) {
        when {
            trackCount < 10 && !searchHistoryTrackList.contains(track) -> {searchHistoryTrackList.add(0, track)
                trackCount += 1}
            trackCount == 10 && !searchHistoryTrackList.contains(track) -> {searchHistoryTrackList.add(0, track)
                searchHistoryTrackList.removeAt(9)}
            else -> {searchHistoryTrackList.remove(track)
                searchHistoryTrackList.add(0, track)}
        }
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_TRACK_KEY, Gson().toJson(searchHistoryTrackList))
            .apply()
    }

}