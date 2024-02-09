package com.example.playlistmaker.db.data.di

import androidx.room.Room
import com.example.playlistmaker.data.TrackToPlayListMediator
import com.example.playlistmaker.data.TrackToPlayListMediatorImpl
import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.db.data.FavoritesRepositoryImpl
import com.example.playlistmaker.db.data.dao.PlayListDao
import com.example.playlistmaker.db.data.dao.TrackDao
import com.example.playlistmaker.db.data.playList.PlayListRepositoryImpl
import com.example.playlistmaker.db.domain.FavoritesRepository
import com.example.playlistmaker.db.domain.PlayListRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module



val dataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "track_table.bd").build()
    }

    single<TrackDao> {
        val appDataBase = get<AppDatabase>()
        appDataBase.trackDao()
    }

    single<PlayListDao> {
        val appDataBase = get<AppDatabase>()
        appDataBase.playListDao()
    }


    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get<TrackDao>())
    }

    single<PlayListRepository> {
        PlayListRepositoryImpl(get<PlayListDao>(), get())
    }

    single<TrackToPlayListMediator> {
        TrackToPlayListMediatorImpl()
    }
}