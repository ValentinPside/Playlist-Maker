package com.example.playlistmaker.search.data.remote

import com.example.playlistmaker.search.data.SearchResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHistoryRemoteDataSourceImpl(
    private val api: ITunesSearchAPI
) : SearchHistoryRemoteDataSource {


    override suspend fun search(query: String): Flow<SearchResponse> = flow {
        emit(api.search(query))
    }

}