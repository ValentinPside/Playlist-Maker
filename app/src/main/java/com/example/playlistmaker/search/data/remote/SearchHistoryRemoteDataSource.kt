package com.example.playlistmaker.search.data.remote

import com.example.playlistmaker.search.data.SearchResponse
import retrofit2.Call

interface SearchHistoryRemoteDataSource {

    fun search(query: String): Call<SearchResponse>

}