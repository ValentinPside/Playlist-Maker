package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.constraintlayout.widget.Group
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.extension.visibleOrGone
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.adapters.SearchAdapter
import debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

const val SEARCH_HISTORY_FILE_NAME = "search_history_file_name"


const val PARCEL_TRACK_KEY = "parcel_track_key"


class SearchFragment: androidx.fragment.app.Fragment() {
    private val searchViewModel: SearchViewModel by viewModel()

    private lateinit var searchTextField: EditText
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
    private val searchDebounce by lazy { debounce(SEARCH_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, true, searchViewModel::onSearchChanged) }

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

    private fun sendRequestToServer() {
        searchDebounce(searchTextField.text.toString())
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
                    searchDebounce(searchTextField.text.toString())
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

            lifecycleScope.launch { searchViewModel.getSearchHistoryTrackList() }
        }

        clearHistoryButton.setOnClickListener {
            lifecycleScope.launch { searchViewModel.clearHistory() }
            historyAdapter.tracks = emptyList()
        }

        communicationProblemButton.setOnClickListener {
            sendRequestToServer()
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                searchViewModel.observe().collect {
                    historyViewGroup.visibleOrGone(it.searchHistoryTrackList.isNotEmpty() && it.placeHolderState == PlaceHolderState.HISTORY)

                    updateStateHolderVisible(it.placeHolderState)

                    historyAdapter.tracks = it.searchHistoryTrackList
                    historyAdapter.notifyDataSetChanged()

                    searchAdapter.tracks = it.trackList
                    searchAdapter.notifyDataSetChanged()
                }
            }
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
        lifecycleScope.launch { searchViewModel.getSearchHistoryTrackList() }
    }

    private fun onClick(track: Track) {
        debounce<Unit>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
            lifecycleScope.launch { searchViewModel.writeHistory(track) }
            val args = bundleOf(PARCEL_TRACK_KEY to track)
            findNavController().navigate(R.id.action_searchFragment_to_audioPlayerFragment, args)
        }.invoke(Unit)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}