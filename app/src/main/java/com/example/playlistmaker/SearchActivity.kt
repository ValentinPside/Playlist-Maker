package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

const val SEARCH_SHARED_PREFS_KEY = "123"

class SearchActivity : AppCompatActivity() {
    private val tracks = ArrayList<Track>()
    private lateinit var searchTextField: EditText
    private lateinit var toolbar: Toolbar
    private lateinit var clearButton: ImageView
    private lateinit var rvSearchTrack: RecyclerView
    private lateinit var nothingFoundPlaceholder: LinearLayout
    private lateinit var communicationProblemPlaceholder: LinearLayout
    private lateinit var communicationProblemButton: Button
    private lateinit var searchHistoryText: TextView
    private lateinit var searchHistoryCleanButton: Button
    private lateinit var searchHistoryRecyclerView: RecyclerView
    lateinit var historyAdapter: TrackAdapter
    private var searchLineText : String? = null
    private val searchHistoryObj = SearchHistory()

    private companion object {
        private const val EDIT_TEXT = "EDIT_TEXT"
        private const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }

    private val trackAdapter = TrackAdapter()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesSearchAPI::class.java)

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
    private fun sendRequestToServer() {
        rvSearchTrack.visibility = View.VISIBLE
        nothingFoundPlaceholder.visibility = View.GONE
        communicationProblemPlaceholder.visibility = View.GONE
        if (searchTextField.text.isNotEmpty()) {
            iTunesService.findTrack(searchTextField.text.toString()).enqueue(object : Callback<TrackResponse> {

                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>){
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                            rvSearchTrack.visibility = View.VISIBLE

                        } else {
                            tracks.clear()
                            rvSearchTrack.visibility = View.GONE
                            nothingFoundPlaceholder.visibility = View.VISIBLE
                        }
                    }
                    else {
                        tracks.clear()
                        rvSearchTrack.visibility = View.GONE
                        communicationProblemPlaceholder.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    tracks.clear()
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
        trackAdapter.tracks = tracks
        rvSearchTrack = findViewById(R.id.rv_search_track)
        rvSearchTrack.adapter = trackAdapter
        nothingFoundPlaceholder = findViewById(R.id.nothing_found_placeholder)
        communicationProblemPlaceholder = findViewById(R.id.communication_problem_placeholder)
        communicationProblemButton = findViewById(R.id.update_button)
        searchHistoryText = findViewById(R.id.textView)
        searchHistoryCleanButton = findViewById(R.id.button)
        searchHistoryRecyclerView = findViewById(R.id.recyclerView)
        historyAdapter = TrackAdapter()
        historyAdapter.tracks = searchHistoryObj.trackHistoryList

        applicationContext.getSharedPreferences(SEARCH_SHARED_PREFS_KEY, MODE_PRIVATE)
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        searchHistoryRecyclerView.adapter = historyAdapter

        searchTextField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchTextField.text.isEmpty() && App.trackHistoryList.isNotEmpty()) {
                historyVisible()
            } else {
                historyInVisible()
            }

        }

        searchTextField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (searchTextField.hasFocus() && p0?.isEmpty() == true && App.trackHistoryList.isNotEmpty()) {
                    historyVisible()
                } else {
                    historyInVisible()
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
            tracks.clear()
            rvSearchTrack.visibility = View.GONE
            historyVisible()
        }
        searchHistoryCleanButton.setOnClickListener {
            App.trackHistoryList.clear()
            historyInVisible()
            historyAdapter.notifyDataSetChanged()
        }

        searchTextField.addTextChangedListener(simpleTextWatcher)

        searchTextField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendRequestToServer()
                true
            }
            false
        }

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
    override fun onSaveInstanceState(outState: Bundle) {
        searchLineText = searchTextField.text.toString()
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, searchLineText)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchLineText = savedInstanceState.getString(EDIT_TEXT,"")
    }

    private fun historyVisible() {
        searchHistoryRecyclerView.visibility = View.VISIBLE
        searchHistoryCleanButton.visibility = View.VISIBLE
        searchHistoryText.visibility = View.VISIBLE
    }

    private fun historyInVisible() {
        searchHistoryRecyclerView.visibility = View.GONE
        searchHistoryCleanButton.visibility = View.GONE
        searchHistoryText.visibility = View.GONE
    }
}