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
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private lateinit var searchTextField: EditText
    private lateinit var toolbar: Toolbar
    private lateinit var clearButton: ImageView
    private lateinit var rvSearchTrack: RecyclerView
    private var searchLineText : String? = null
    private lateinit var nothingFoundPlaceholder: LinearLayout
    private lateinit var communicationProblemPlaceholder: LinearLayout
    private lateinit var searchAdapter: SearchAdapter

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

    private fun sendRequestToServer() {
        rvSearchTrack.visibility = View.VISIBLE
        nothingFoundPlaceholder.visibility = View.GONE
        communicationProblemPlaceholder.visibility = View.GONE
        if (searchTextField.text.isNotEmpty()) {
            iTunesService.search(searchTextField.text.toString()).enqueue(object : Callback<SearchResponse> {

                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>){
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
        nothingFoundPlaceholder = findViewById(R.id.nothing_found_placeholder)
        communicationProblemPlaceholder = findViewById(R.id.communication_problem_placeholder)
        val communicationProblemButton = findViewById<Button>(R.id.update_button)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_search_track)
        recyclerView.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAdapter(trackList)
        recyclerView.adapter = searchAdapter


        searchTextField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

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
}