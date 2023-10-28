package com.example.playlistmaker.search.data.remote

import com.example.playlistmaker.search.data.SearchResponse
import retrofit2.Call

class SearchHistoryRemoteDataSourceImpl(
    private val api: ITunesSearchAPI
) : SearchHistoryRemoteDataSource {


    override fun search(query: String): Call<SearchResponse> {
        return api.search(query)
    }
}