package com.example.playlistmaker.data

import com.example.playlistmaker.data.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchAPI {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<SearchResponse>

}