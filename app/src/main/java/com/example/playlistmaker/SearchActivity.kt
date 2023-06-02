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
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private val tracks = ArrayList<Track>()
    private lateinit var searchTextField: EditText

    private companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }

    var searchLineText : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val clearButton = findViewById<ImageView>(R.id.clear_button)
        searchTextField = findViewById(R.id.search_field)
        val trackAdapter = TrackAdapter()
        trackAdapter.tracks = tracks
        val rvSearchTrack = findViewById<RecyclerView>(R.id.rv_search_track)
        rvSearchTrack.adapter = trackAdapter
        val nothingFoundPlaceholder = findViewById<LinearLayout>(R.id.nothing_found_placeholder)
        val communicationProblemPlaceholder = findViewById<LinearLayout>(R.id.communication_problem_placeholder)
        val communicationProblemButton = findViewById<Button>(R.id.update_button)
        val retrofit = Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val iTunesService = retrofit.create(iTunesSearchAPI::class.java)

        val simpleTextWatcher = object : TextWatcher {
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

        searchTextField.addTextChangedListener(simpleTextWatcher)

        clearButton.setOnClickListener {
            searchTextField.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val view: View? = this.currentFocus
            inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
        }
         fun sendRequestToServer() {
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
        searchTextField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                sendRequestToServer()
                true
            }
            false
        }

        communicationProblemButton.setOnClickListener {
            sendRequestToServer()
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
}