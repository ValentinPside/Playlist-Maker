package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(sharedPreferences : SharedPreferences) {

    private val gson = Gson()
    val historyTrackList : List<Track> = read(sharedPreferences)


    // чтение
    private fun read(sharedPreferences : SharedPreferences): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_TRACK_KEY, null) ?: return emptyList<Track>()
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(json, type)
    }

    // запись
    private fun write(sharedPreferences : SharedPreferences) {
        val json = Gson().toJson(historyTrackList)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_TRACK_KEY, json)
            .apply()
    }

}