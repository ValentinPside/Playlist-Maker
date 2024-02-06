package com.example.playlistmaker.mediateca

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.MediatecaPlaylistsBinding
import com.example.playlistmaker.extension.visibleOrInvisible
import com.example.playlistmaker.mediateca.ui.PlayListViewModel
import com.example.playlistmaker.mediateca.ui.playlist.PlayListAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayListsFragment : Fragment(R.layout.mediateca_playlists) {

    private val viewModel: PlayListViewModel by viewModel{ parametersOf() }

    private val binding by viewBinding(MediatecaPlaylistsBinding::bind)
    private lateinit var adapter: PlayListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPlayListRecyclerView()
        binding.newPlayListBut.setOnClickListener {
            findNavController().navigate(R.id.action_playListsFragment_to_newPlaylistFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeUi().collect { state ->
                    binding.nothingFoundImage.visibleOrInvisible(state.playListItems.isEmpty())
                    binding.nothingCreatedText.visibleOrInvisible(state.playListItems.isEmpty())
                    adapter.submitList(state.playListItems)
                }
            }
        }
    }

    private fun setupPlayListRecyclerView() {
        adapter = PlayListAdapter { openDetails(it.id) }
        binding.playListRecyclerView.adapter = adapter
    }

    private fun openDetails(playlistId: Int) {
        val params = bundleOf(
            "playlistId" to playlistId
        )
        findNavController()
            .navigate(R.id.action_playListsFragment_to_playlistDetailFragment, params)
    }

    companion object {
        fun newInstance() = PlayListsFragment()
    }

}