package com.example.playlistmaker.mapTheme.domain

import com.example.playlistmaker.mapTheme.domain.models.MapTheme

interface SetMapThemeUseCase {

    operator fun invoke(mapTheme: MapTheme)

}

class SetMapThemeUseCaseImpl(
    private val repository: MapThemeRepository
) : SetMapThemeUseCase {
    override fun invoke(mapTheme: MapTheme) {
        repository.setMapTheme(mapTheme)
    }
}