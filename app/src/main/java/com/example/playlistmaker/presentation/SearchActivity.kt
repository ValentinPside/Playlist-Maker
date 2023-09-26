package com.example.playlistmaker.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.*
import com.example.playlistmaker.data.ITunesSearchAPI
import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.data.SearchResponse
import com.example.playlistmaker.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SEARCH_HISTORY_FILE_NAME = "search_history_file_name"

const val SEARCH_HISTORY_TRACK_KEY = "search_history_track_key"

const val PARCEL_TRACK_KEY = "parcel_track_key"


class SearchActivity : AppCompatActivity(), SearchAdapter.Listener {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private lateinit var searchTextField: EditText
    private lateinit var toolbar: Toolbar
    private lateinit var clearButton: ImageView
    private lateinit var rvSearchTrack: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private var searchLineText : String? = null
    private lateinit var nothingFoundPlaceholder: LinearLayout
    private lateinit var communicationProblemPlaceholder: LinearLayout
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var historyAdapter: SearchAdapter
    private lateinit var historyViewGroup : Group
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var searchHistory : SearchHistory
    private lateinit var searchHistoryTrackList : ArrayList<Track>
    private lateinit var progressBar : ProgressBar

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val trackList = ArrayList<Track>()

    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesSearchAPI::class.java)


    private val simpleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // empty
        }

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

    private fun clickDebounce() : Boolean {
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
        rvSearchTrack.visibility = View.GONE
        nothingFoundPlaceholder.visibility = View.GONE
        communicationProblemPlaceholder.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        if (searchTextField.text.isNotEmpty()) {
            iTunesService.search(searchTextField.text.toString()).enqueue(object : Callback<SearchResponse> {

                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>){
                    progressBar.visibility = View.GONE
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            rvSearchTrack.visibility = View.VISIBLE

                        } else {
                            trackList.clear()
                            rvSearchTrack.visibility = View.GONE
                            nothingFoundPlaceholder.visibility = View.VISIBLE
                        }
                    }
                    else {
                        trackList.clear()
                        rvSearchTrack.visibility = View.GONE
                        communicationProblemPlaceholder.visibility = View.VISIBLE
                    }
                    searchAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    trackList.clear()
                    rvSearchTrack.visibility = View.GONE
                    communicationProblemPlaceholder.visibility = View.VISIBLE }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
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
        sharedPreferences = getSharedPreferences(SEARCH_HISTORY_FILE_NAME, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)
        searchHistoryTrackList =
            if(searchHistory.historyTrackList.isEmpty()){
                ArrayList()}
            else{searchHistory.historyTrackList as ArrayList<Track>
            }
        val communicationProblemButton = findViewById<Button>(R.id.update_button)
        val clearHistoryButton = findViewById<Button>(R.id.button)
        rvSearchTrack.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAdapter(trackList, this)
        historyAdapter = SearchAdapter(searchHistoryTrackList, this)
        rvSearchTrack.adapter = searchAdapter
        historyRecyclerView.adapter = historyAdapter

        if(searchHistoryTrackList.isNotEmpty()){
            historyViewGroup.visibility = View.VISIBLE
        }



        searchTextField.setOnFocusChangeListener { view, hasFocus ->
            historyViewGroup.visibility = if (hasFocus && searchTextField.text.isEmpty() && searchHistoryTrackList.isNotEmpty()) View.VISIBLE else View.GONE
        }

        searchTextField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                historyViewGroup.visibility = if (searchTextField.hasFocus() && p0?.isEmpty() == true) View.VISIBLE else View.GONE
                if (searchTextField.text.isNotBlank()){
                    searchDebounce()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })


        clearButton.setOnClickListener {
            searchTextField.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val view: View? = this.currentFocus
            inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
            rvSearchTrack.visibility = View.GONE
            if(searchHistoryTrackList.isEmpty()){
                historyViewGroup.visibility = View.GONE
            }
        }

        clearHistoryButton.setOnClickListener{
            sharedPreferences.edit().clear().apply()
            historyViewGroup.visibility = View.GONE
            historyAdapter.tracks.clear()
            searchHistoryTrackList.clear()
            historyAdapter.notifyDataSetChanged()

        }

        searchTextField.addTextChangedListener(simpleTextWatcher)

        communicationProblemButton.setOnClickListener {
            sendRequestToServer()
        }

    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onClick(track: Track) {
        if (clickDebounce()) {
            searchHistory.write(sharedPreferences, searchHistoryTrackList, track)
            historyAdapter.notifyDataSetChanged()
            val intent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
            intent.putExtra(PARCEL_TRACK_KEY, track)
            startActivity(intent)
        }
    }
}