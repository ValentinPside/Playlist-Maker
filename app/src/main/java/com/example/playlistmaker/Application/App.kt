package com.example.playlistmaker.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.search.di.data.searchTracksRepositoryModule
import com.example.playlistmaker.search.di.searchViewModelModule
import com.example.playlistmaker.settings.di.settingsViewModelModule
import com.example.playlistmaker.sharing.di.playListFragmentViewModelModule
import com.example.playlistmaker.sharing.di.tracksFragmentViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(settingsViewModelModule, playerViewModelModule, searchTracksRepositoryModule, searchViewModelModule, tracksFragmentViewModelModule, playListFragmentViewModelModule)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}