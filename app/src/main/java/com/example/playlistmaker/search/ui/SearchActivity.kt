package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.*
import com.example.playlistmaker.extension.visibleOrGone
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.data.SearchTracksRepositoryImpl
import com.example.playlistmaker.search.data.remote.ITunesSearchAPI
import com.example.playlistmaker.search.data.remote.SearchHistoryRemoteDataSource
import com.example.playlistmaker.search.data.remote.SearchHistoryRemoteDataSourceImpl
import com.example.playlistmaker.search.domain.ClearTracksHistoryUseCase
import com.example.playlistmaker.search.domain.ClearTracksHistoryUseCaseImpl
import com.example.playlistmaker.search.domain.GetTracksHistoryUseCase
import com.example.playlistmaker.search.domain.GetTracksHistoryUseCaseImpl
import com.example.playlistmaker.search.domain.SearchTracksUseCase
import com.example.playlistmaker.search.domain.SearchTracksUseCaseImpl
import com.example.playlistmaker.search.domain.WriteTracksHistoryUseCase
import com.example.playlistmaker.search.domain.WriteTracksHistoryUseCaseImpl
import com.example.playlistmaker.search.ui.adapters.SearchAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SEARCH_HISTORY_FILE_NAME = "search_history_file_name"


const val PARCEL_TRACK_KEY = "parcel_track_key"


class SearchActivity: AppCompatActivity() {

    private lateinit var searchViewModel: SearchViewModel

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private lateinit var searchTextField: EditText
    private lateinit var toolbar: Toolbar
    private lateinit var clearButton: ImageView
    private lateinit var rvSearchTrack: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private var searchLineText: String? = null
    private lateinit var nothingFoundPlaceholder: LinearLayout
    private lateinit var communicationProblemPlaceholder: LinearLayout
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var historyAdapter: SearchAdapter
    private lateinit var historyViewGroup: Group
    private lateinit var progressBar: ProgressBar

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val simpleTextWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.isNullOrEmpty()) {
                clearButton.visibility = clearButtonVisibility(s)
            } else {
                val input = s.toString()
                clearButton.isVisible = !input.isEmpty()
            }
            clearButton.visibility = clearButtonVisibility(s)
        }

        override fun afterTextChanged(s: Editable?) {
            searchLineText = s?.toString()
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private val searchRunnable = Runnable { sendRequestToServer() }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun sendRequestToServer() {
        searchViewModel.showProgressBar()
        searchViewModel.onSearchChanged(searchTextField.text.toString())
    }

    private fun initDeps(){

        //remote
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val iTunesService = retrofit.create(ITunesSearchAPI::class.java)
        val remoteDataStore: SearchHistoryRemoteDataSource = SearchHistoryRemoteDataSourceImpl(iTunesService)

        //local
        val sharedPreferences = getSharedPreferences(SEARCH_HISTORY_FILE_NAME, MODE_PRIVATE)

        //repository
        val searchHistoryRepository = SearchTracksRepositoryImpl(sharedPreferences, remoteDataStore)

        //use cases
        val getTracksHistoryUseCase: GetTracksHistoryUseCase = GetTracksHistoryUseCaseImpl(searchHistoryRepository)
        val clearTracksHistoryUseCase: ClearTracksHistoryUseCase = ClearTracksHistoryUseCaseImpl(searchHistoryRepository)
        val writeHistoryUseCase: WriteTracksHistoryUseCase = WriteTracksHistoryUseCaseImpl(searchHistoryRepository)
        val searchTracksUseCase: SearchTracksUseCase = SearchTracksUseCaseImpl(searchHistoryRepository)

        val viewModelFactory = SearchViewModelFactory(
            getTracksHistoryUseCase,
            clearTracksHistoryUseCase,
            writeHistoryUseCase,
            searchTracksUseCase
        )

        searchViewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initDeps()
        initViews()
        val communicationProblemButton = findViewById<Button>(R.id.update_button)
        val clearHistoryButton = findViewById<Button>(R.id.button)

        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        searchTextField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                historyViewGroup.visibility =
                    if (searchTextField.hasFocus() && p0?.isEmpty() == true) View.VISIBLE else View.GONE
                if (searchTextField.text.isNotBlank()) {
                    searchDebounce()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        searchTextField.addTextChangedListener(simpleTextWatcher)

        initTrackList()
        initSearchHistoryTrackList()

        clearButton.setOnClickListener {
            searchTextField.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val view: View? = this.currentFocus
            inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)

            searchViewModel.getSearchHistoryTrackList()
        }

        clearHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
            historyAdapter.tracks = emptyList()
        }

        communicationProblemButton.setOnClickListener {
            sendRequestToServer()
        }

        searchViewModel.observe().observe(this) {
            historyViewGroup.visibleOrGone(it.searchHistoryTrackList.isNotEmpty() && it.placeHolderState == PlaceHolderState.HISTORY)

            updateStateHolderVisible(it.placeHolderState)

            historyAdapter.tracks = it.searchHistoryTrackList
            historyAdapter.notifyDataSetChanged()

            searchAdapter.tracks = it.trackList
            searchAdapter.notifyDataSetChanged()
        }

    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        clearButton = findViewById(R.id.clear_button)
        searchTextField = findViewById(R.id.search_field)
        rvSearchTrack = findViewById(R.id.rv_search_track)
        historyRecyclerView = findViewById(R.id.recyclerView)
        nothingFoundPlaceholder = findViewById(R.id.nothing_found_placeholder)
        communicationProblemPlaceholder = findViewById(R.id.communication_problem_placeholder)
        historyViewGroup = findViewById(R.id.history_group)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun updateStateHolderVisible(state: PlaceHolderState) {
        rvSearchTrack.visibleOrGone(state == PlaceHolderState.SEARCH_TRACK)
        nothingFoundPlaceholder.visibleOrGone(state == PlaceHolderState.NOTHING_FOUND)
        communicationProblemPlaceholder.visibleOrGone(state == PlaceHolderState.PROBLEM)
        progressBar.visibleOrGone(state == PlaceHolderState.LOADING)
    }

    private fun initTrackList() {
        searchAdapter = SearchAdapter(emptyList()) { onClick(it) }
        rvSearchTrack.adapter = searchAdapter
    }

    private fun initSearchHistoryTrackList() {
        historyAdapter = SearchAdapter(arrayListOf()) { onClick(it) }
        historyRecyclerView.adapter = historyAdapter
        searchViewModel.getSearchHistoryTrackList()
    }

    private fun onClick(track: Track) {
        if (clickDebounce()) {
            searchViewModel.writeHistory(track)

            val intent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
            intent.putExtra(PARCEL_TRACK_KEY, track)
            startActivity(intent)
        }
    }
}