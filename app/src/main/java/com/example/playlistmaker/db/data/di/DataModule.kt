package com.example.playlistmaker.db.data.di

import androidx.room.Room
import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.db.data.FavoritesRepositoryImpl
import com.example.playlistmaker.db.data.TrackDbConvertor
import com.example.playlistmaker.db.data.dao.TrackDao
import com.example.playlistmaker.db.domain.FavoritesRepository
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

    factory {
        TrackDbConvertor()
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get<TrackDao>(), get<TrackDbConvertor>())
    }
}