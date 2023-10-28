package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.mapTheme.domain.GetMapThemeUseCase
import com.example.playlistmaker.mapTheme.domain.SetMapThemeUseCase

class SettingsViewModelFactory(
    private val getMapThemeUseCase: GetMapThemeUseCase,
    private val setMapThemeUseCase: SetMapThemeUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(getMapThemeUseCase, setMapThemeUseCase) as T
    }
}