package com.example.playlistmaker.mediateca.ui.tracks

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.MediatecaTracksBinding
import com.example.playlistmaker.extension.visibleOrGone
import com.example.playlistmaker.extension.visibleOrInvisible
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.PARCEL_TRACK_KEY
import com.example.playlistmaker.search.ui.adapters.SearchAdapter
import debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TracksFragment : Fragment(R.layout.mediateca_tracks) {

    private val viewModel: TracksViewModel by viewModel{
        parametersOf()
    }

    private val binding by viewBinding(MediatecaTracksBinding::bind)
    private lateinit var adapter: SearchAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState().collect {state ->
                    adapter.tracks = state.items
                    adapter.notifyDataSetChanged()

                    binding.mediatecaGroup.visibleOrInvisible(state.items.isEmpty())
                    binding.favoritesTracksRv.visibleOrGone(state.items.isNotEmpty())
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter(emptyList(), ::onClickTrack)
        binding.favoritesTracksRv.adapter = adapter
        binding.favoritesTracksRv.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesTracksRv.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
    }

    private fun onClickTrack(track: Track) {
        debounce<Unit>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
            val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
            intent.putExtra(PARCEL_TRACK_KEY, track)
            startActivity(intent)
        }.invoke(Unit)
    }

    companion object {
        fun newInstance() = TracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}