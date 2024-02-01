package com.example.playlistmaker.mediateca.ui.tracks

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.MediatecaTracksBinding
import com.example.playlistmaker.search.domain.models.Track
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

                    if(state.items.isEmpty()){
                        binding.mediatecaGroup.visibility = View.VISIBLE
                        binding.favoritesTracksRv.visibility = View.INVISIBLE
                    } else{ binding.favoritesTracksRv.visibility = View.VISIBLE
                        binding.mediatecaGroup.visibility = View.INVISIBLE}
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
//            val intent = Intent(requireContext(), AudioPlayerFragment::class.java)
//            intent.putExtra(PARCEL_TRACK_KEY, track)
//            startActivity(intent)
            val args = bundleOf("parcel_track_key" to track)
            findNavController().navigate(R.id.action_playListsFragment_to_audioPlayerFragment, args)
        }.invoke(Unit)
    }

    companion object {
        fun newInstance() = TracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}