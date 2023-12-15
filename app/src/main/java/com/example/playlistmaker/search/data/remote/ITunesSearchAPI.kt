package com.example.playlistmaker.search.data.remote

import com.example.playlistmaker.search.data.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchAPI {

    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): SearchResponse

}