package com.example.playlistmaker.search.data.remote

import com.example.playlistmaker.search.data.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchAPI {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<SearchResponse>

}