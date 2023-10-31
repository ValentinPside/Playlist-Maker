package com.example.playlistmaker.mapTheme.domain

import com.example.playlistmaker.mapTheme.domain.models.MapTheme

interface MapThemeRepository {

    fun get(): MapTheme

    fun setMapTheme(mapTheme: MapTheme)

}