package com.example.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(sharedPreferences : SharedPreferences) {

    val historyTrackList : List<Track> = read(sharedPreferences)

    // чтение
    private fun read(sharedPreferences : SharedPreferences): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_TRACK_KEY, null) ?: return emptyList<Track>()
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    // запись
     fun write(sharedPreferences : SharedPreferences, searchHistoryTrackList : ArrayList<Track>, track: Track) {
        if(searchHistoryTrackList.size < 10 && !searchHistoryTrackList.contains(track)){
            searchHistoryTrackList.add(0, track)
        }
        if(searchHistoryTrackList.size == 10 && !searchHistoryTrackList.contains(track)){
            searchHistoryTrackList.add(0, track)
            searchHistoryTrackList.removeAt(10)
        }
        if(searchHistoryTrackList.contains(track)){
            searchHistoryTrackList.remove(track)
            searchHistoryTrackList.add(0, track)
        }
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_TRACK_KEY, Gson().toJson(searchHistoryTrackList))
            .apply()
    }

}