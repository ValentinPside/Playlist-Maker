package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.graphics.Insets
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.extension.visibleOrGone
import com.example.playlistmaker.main.ui.MainActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.ui.adapters.SearchAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

const val SEARCH_HISTORY_FILE_NAME = "search_history_file_name"


const val PARCEL_TRACK_KEY = "parcel_track_key"


class SearchFragment: androidx.fragment.app.Fragment() {
    private val searchViewModel: SearchViewModel by viewModel()

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

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        val communicationProblemButton = binding.updateButton
        val clearHistoryButton = binding.button

        historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
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
            val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val view: View? = this.requireView().findFocus()
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

        searchViewModel.observe().observe(viewLifecycleOwner) {
            historyViewGroup.visibleOrGone(it.searchHistoryTrackList.isNotEmpty() && it.placeHolderState == PlaceHolderState.HISTORY)

            updateStateHolderVisible(it.placeHolderState)

            historyAdapter.tracks = it.searchHistoryTrackList
            historyAdapter.notifyDataSetChanged()

            searchAdapter.tracks = it.trackList
            searchAdapter.notifyDataSetChanged()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        clearButton = binding.clearButton
        searchTextField = binding.searchField
        rvSearchTrack = binding.rvSearchTrack
        historyRecyclerView = binding.recyclerView
        nothingFoundPlaceholder = binding.nothingFoundPlaceholder
        communicationProblemPlaceholder = binding.communicationProblemPlaceholder
        historyViewGroup = binding.historyGroup
        progressBar = binding.progressBar
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
            val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
            intent.putExtra(PARCEL_TRACK_KEY, track)
            startActivity(intent)
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}