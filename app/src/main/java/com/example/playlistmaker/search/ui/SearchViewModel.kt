package com.example.playlistmaker.search.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.ClearTracksHistoryUseCase
import com.example.playlistmaker.search.domain.GetTracksHistoryUseCase
import com.example.playlistmaker.search.domain.SearchTracksUseCase
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.WriteTracksHistoryUseCase

class SearchViewModel (
    private val getTracksHistoryUseCase: GetTracksHistoryUseCase,
    private val clearTracksHistoryUseCase: ClearTracksHistoryUseCase,
    private val writeTracksHistoryUseCase: WriteTracksHistoryUseCase,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {

    private var screenPaused = false

    private val viewState = MutableLiveData(ViewState())

    fun observe(): LiveData<ViewState> = viewState

    init {
        getSearchHistoryTrackList()
    }

    fun showProgressBar(){
        viewState.value = viewState.value?.copy(placeHolderState = PlaceHolderState.LOADING)
    }

    fun showCommunicationProblemPlaceholder(){
        viewState.value = viewState.value?.copy(placeHolderState = PlaceHolderState.PROBLEM)
    }

    fun onSearchChanged(query : String) {
        if(screenPaused){
            return
        }
        searchTracksUseCase.invoke(query, ::showCommunicationProblemPlaceholder) { trackList ->
                val placeHolderState = if (trackList.isNotEmpty()) PlaceHolderState.SEARCH_TRACK else PlaceHolderState.NOTHING_FOUND
                viewState.value = viewState.value?.copy(trackList = trackList, placeHolderState = placeHolderState)
        }
    }

    fun getSearchHistoryTrackList(){
        viewState.value = viewState.value?.copy(searchHistoryTrackList = getTracksHistoryUseCase.get(), placeHolderState = PlaceHolderState.HISTORY)
    }

    fun clearHistory() {
        clearTracksHistoryUseCase()
        getSearchHistoryTrackList()
    }

    fun writeHistory(track: Track) {
        val trackList = viewState.value?.searchHistoryTrackList ?: emptyList()
        writeTracksHistoryUseCase.write(trackList, track)
        getSearchHistoryTrackList()
    }

    fun onFragmentResume(){
        screenPaused = false
    }

    fun onFragmentPaused(){
        screenPaused = true
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