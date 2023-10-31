package com.example.playlistmaker.search.di.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.search.data.SearchTracksRepository
import com.example.playlistmaker.search.data.SearchTracksRepositoryImpl
import com.example.playlistmaker.search.data.remote.ITunesSearchAPI
import com.example.playlistmaker.search.data.remote.SearchHistoryRemoteDataSource
import com.example.playlistmaker.search.data.remote.SearchHistoryRemoteDataSourceImpl
import com.example.playlistmaker.search.ui.SEARCH_HISTORY_FILE_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchTracksRepositoryModule = module {

    factory<SearchTracksRepository> {
        SearchTracksRepositoryImpl(provideSharedPref(androidApplication()), get())
    }

    single<SearchHistoryRemoteDataSource> {
        SearchHistoryRemoteDataSourceImpl(iTunesService)
    }

}

private val retrofit = Retrofit.Builder()
    .baseUrl("https://itunes.apple.com")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private val iTunesService = retrofit.create(ITunesSearchAPI::class.java)

fun provideSharedPref(app: Application): SharedPreferences {
    return app.applicationContext.getSharedPreferences(
        SEARCH_HISTORY_FILE_NAME,
        Context.MODE_PRIVATE
    )
}
