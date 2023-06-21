package com.example.playlistmaker

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistory {
    private val savedHistory = App.getSharedPreferences()
    private val gson = Gson()
    private var json = ""
    var counter = 0
    var trackHistoryList = App.trackHistoryList

    fun editArray(newHistoryTrack: Track) {
        if (json.isNotEmpty()) {
            if (trackHistoryList.isEmpty()) {
                if (savedHistory.contains(SEARCH_SHARED_PREFS_KEY)) {
                    val type = object : TypeToken<ArrayList<Track>>() {}.type
                    trackHistoryList = gson.fromJson(json, type)
                }
            }
        }
        if (trackHistoryList.contains(newHistoryTrack)) {
            trackHistoryList.remove(newHistoryTrack)
            trackHistoryList.add(0, newHistoryTrack)
        } else {
            if (trackHistoryList.size < 10) trackHistoryList.add(0, newHistoryTrack)
            else {
                trackHistoryList.removeAt(9)
                trackHistoryList.add(0, newHistoryTrack)
            }
        }
        saveHistory()
    }

    private fun saveHistory() {
        json = gson.toJson(trackHistoryList)

        savedHistory.edit()
            .clear()
            .putString(SEARCH_SHARED_PREFS_KEY, json)
            .apply()
        counter = trackHistoryList.size
    }

    fun toaster(context: Context, text: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(context, text, duration)
        toast.show()
    }
}