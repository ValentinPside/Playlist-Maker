package com.example.playlistmaker.search.data.remote

import com.example.playlistmaker.search.data.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class SearchHistoryRemoteDataSourceImpl(
    private val api: ITunesSearchAPI
) : SearchHistoryRemoteDataSource {


    override suspend fun search(query: String): SearchResponse {
        return api.search(query)
    }
}