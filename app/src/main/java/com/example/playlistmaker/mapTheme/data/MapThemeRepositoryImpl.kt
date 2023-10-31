package com.example.playlistmaker.mapTheme.data

import android.content.Context
import com.example.playlistmaker.mapTheme.domain.MapThemeRepository
import com.example.playlistmaker.mapTheme.domain.models.MapTheme

class MapThemeRepositoryImpl(context: Context) : MapThemeRepository {
    private val pref = context.getSharedPreferences(MAP_THEME_NAME, Context.MODE_PRIVATE)

    override fun get(): MapTheme {
        val isDarkTheme = pref.getBoolean(MAP_THEME_KEY, false)
        return MapTheme(darkTheme = isDarkTheme)
    }

    override fun setMapTheme(mapTheme: MapTheme) {
        pref.edit().putBoolean(MAP_THEME_KEY, mapTheme.darkTheme).apply()
    }

    companion object {
        private const val MAP_THEME_NAME = "map_theme"
        private const val MAP_THEME_KEY = "mapTheme"

    }

}