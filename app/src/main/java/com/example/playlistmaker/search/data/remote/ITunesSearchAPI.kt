package com.example.playlistmaker.search.data.remote

import com.example.playlistmaker.search.data.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchAPI {

    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): SearchResponse

}