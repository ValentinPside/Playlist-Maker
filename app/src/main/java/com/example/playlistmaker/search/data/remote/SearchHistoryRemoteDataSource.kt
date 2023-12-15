package com.example.playlistmaker.search.data.remote

import com.example.playlistmaker.search.data.SearchResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

interface SearchHistoryRemoteDataSource {

    suspend fun search(query: String): SearchResponse

}