package com.example.playlistmaker.search.data.remote

import com.example.playlistmaker.search.data.SearchResponse
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRemoteDataSource {

    suspend fun search(query: String): Flow<SearchResponse>

}