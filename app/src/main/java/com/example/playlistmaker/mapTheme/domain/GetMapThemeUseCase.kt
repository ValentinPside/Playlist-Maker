package com.example.playlistmaker.mapTheme.domain

import com.example.playlistmaker.mapTheme.domain.models.MapTheme

interface GetMapThemeUseCase {
    operator fun invoke(): MapTheme
}

class GetMapThemeUseCaseImpl(
    private val mapThemeRepository: MapThemeRepository
): GetMapThemeUseCase {

    override operator fun invoke(): MapTheme {
        return mapThemeRepository.get()
    }


}