package com.example.playlistmaker.search.ui


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.ClearTracksHistoryUseCase
import com.example.playlistmaker.search.domain.GetTracksHistoryUseCase
import com.example.playlistmaker.search.domain.SearchTracksUseCase
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.WriteTracksHistoryUseCase
import debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel (
    private val getTracksHistoryUseCase: GetTracksHistoryUseCase,
    private val clearTracksHistoryUseCase: ClearTracksHistoryUseCase,
    private val writeTracksHistoryUseCase: WriteTracksHistoryUseCase,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {

    private val viewState = MutableStateFlow(ViewState())

    fun observe() = viewState.asStateFlow()

    init {
        getSearchHistoryTrackList()
    }

    fun showProgressBar(){
        viewState.update { it.copy(placeHolderState = PlaceHolderState.LOADING) }
    }

    fun showCommunicationProblemPlaceholder(){
        viewState.update { it.copy(placeHolderState = PlaceHolderState.PROBLEM) }
    }

    fun onSearchChanged(query: String) {
        viewModelScope.launch {
            showProgressBar()
            searchTracksUseCase.invoke(query, ::showCommunicationProblemPlaceholder) { trackList ->
                val placeHolderState = if (trackList.isNotEmpty()) PlaceHolderState.SEARCH_TRACK else PlaceHolderState.NOTHING_FOUND
                viewState.update { it.copy(trackList = trackList, placeHolderState = placeHolderState) }
            }
        }
    }

    fun getSearchHistoryTrackList(){
        viewState.update { it.copy(searchHistoryTrackList = getTracksHistoryUseCase.get(), placeHolderState = PlaceHolderState.HISTORY) }
    }

    fun clearHistory() {
        clearTracksHistoryUseCase()
        getSearchHistoryTrackList()
    }

    fun writeHistory(track: Track) {
        val trackList = viewState.value.searchHistoryTrackList
        writeTracksHistoryUseCase.write(trackList, track)
        getSearchHistoryTrackList()
    }

}


data class ViewState(
    val trackList: List<Track> = emptyList(),
    val searchHistoryTrackList: List<Track> = emptyList(),
    val placeHolderState: PlaceHolderState = PlaceHolderState.HISTORY,
)

enum class PlaceHolderState {
    PROBLEM, NOTHING_FOUND, SEARCH_TRACK, LOADING, HISTORY
}