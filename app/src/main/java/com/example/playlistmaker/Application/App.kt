package com.example.playlistmaker.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.db.data.di.dataModule
import com.example.playlistmaker.mediateca.di.newPlayListFragmentViewModelModule
import com.example.playlistmaker.mediateca.di.playListViewModelModule
import com.example.playlistmaker.mediateca.di.tracksViewModelModule
import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.playlistDetails.di.playlistDetailModule
import com.example.playlistmaker.search.di.data.searchTracksRepositoryModule
import com.example.playlistmaker.search.di.searchViewModelModule
import com.example.playlistmaker.settings.di.settingsViewModelModule
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        PermissionRequester.initialize(applicationContext)
        startKoin {
            androidContext(this@App)
            modules(settingsViewModelModule, playerViewModelModule, searchTracksRepositoryModule,
                searchViewModelModule, tracksViewModelModule, playListViewModelModule,
            dataModule, newPlayListFragmentViewModelModule, playlistDetailModule
            )
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