package com.example.playlistmaker.search.creator

import com.example.playlistmaker.search.data.remote.ITunesSearchAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {
    fun createRetrofit() : Retrofit {
        val baseUrl = "https://itunes.apple.com"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createiTunesService() : ITunesSearchAPI {
        return createRetrofit().create(ITunesSearchAPI::class.java)
    }
}