package com.example.playlistmaker.playlistDetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailBinding
import com.example.playlistmaker.extension.visibleOrInvisible
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.adapters.SearchAdapter
import com.example.playlistmaker.uttils.StringUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*
import java.util.concurrent.TimeUnit

class PlaylistDetailFragment : Fragment(R.layout.fragment_playlist_detail) {

    private val binding by viewBinding(FragmentPlaylistDetailBinding::bind)
    private val playlistId by lazy { requireArguments().getInt("playlistId") }
    private val viewModel: PlaylistDetailViewModel by viewModel { parametersOf(playlistId) }

    private lateinit var adapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTracks()
        setupTracksBottomSheet()
        setupMore()

        binding.back.setOnClickListener { findNavController().popBackStack() }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeUi().collect { state ->

                    if (state.isDeleted) {
                        findNavController().popBackStack()
                        return@collect
                    }

                    val tracksCount = StringUtils.getTracksAddition(state.count)

                    adapter.tracks = state.tracks
                    adapter.notifyDataSetChanged()

                    if(state.tracks.isEmpty()){
                        binding.noTracksInPlayListTextView.visibility = View.VISIBLE
                        binding.tracks.visibility = View.INVISIBLE
                    }
                    else{
                        binding.noTracksInPlayListTextView.visibility = View.INVISIBLE
                        binding.tracks.visibility = View.VISIBLE
                    }

                    binding.title.text = state.title
                    binding.description.text = state.description

                    binding.name.text = state.title
                    binding.tracksCount.text = "${state.tracks.size} $tracksCount"

                    Glide.with(requireContext())
                        .load(state.image)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.imagePlayList)

                    val duration = StringUtils.getMinutesAddition(state.duration)
                    binding.duration.text = "${state.duration} $duration • ${state.count} $tracksCount"

                    Glide.with(requireContext())
                        .load(state.image)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.image)
                }
            }
        }

        binding.share.setOnClickListener {
            shareTracks(viewModel.observeUi().value)
        }
    }

    private fun setupMore() {

        val container = binding.moreBottomSheet
        val behavior = BottomSheetBehavior.from(container)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.expandedOffset = 300
        behavior.skipCollapsed = true

        binding.more.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, newState: Int) {
                binding.overlay.visibleOrInvisible(newState == BottomSheetBehavior.STATE_EXPANDED)
            }
            override fun onSlide(p0: View, p1: Float) {}
        })

        binding.textShare.setOnClickListener {
            shareTracks(viewModel.observeUi().value)
        }

        binding.delete.setOnClickListener {
            showDeletePlaylistDialog(viewModel.observeUi().value.title)
        }
    }

    private fun shareTracks(state: State) {

        val tracks = state.tracks

        if (tracks.isEmpty()) {
            Toast.makeText(requireContext(), "В этом плейлисте нет списка треков, которым можно поделиться", Toast.LENGTH_SHORT).show()
            return
        }

        val textBuilder = StringBuilder()

        textBuilder
            .append(state.title)
            .append("\n")

        if (state.description.isNotEmpty()) {
            textBuilder
                .append(state.description)
                .append("\n")
        }

        val tracksCount = StringUtils.getTracksAddition(state.tracks.size)
        textBuilder.append("${state.tracks.size} $tracksCount\n")

        state.tracks.forEachIndexed { index, track ->
            val duration = track.trackTimeMillis?.let {
                String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(track.trackTimeMillis),
                    TimeUnit.MILLISECONDS.toSeconds(track.trackTimeMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(track.trackTimeMillis))
                )
            }.orEmpty()


            textBuilder
                .append("${index + 1}. ${track.artistName} = ${track.trackName} (${duration})")
                .append("\n")
        }


        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textBuilder.toString())
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun setupTracksBottomSheet() {
        val container = binding.tracksBottomSheet
        BottomSheetBehavior.from(container).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            isHideable = false
            peekHeight = 550
        }

        binding.edit.setOnClickListener {
            val args = bundleOf(
                "playlistId" to playlistId
            )
            findNavController().navigate(R.id.action_playlistDetailFragment_to_newPlaylistFragment, args)
        }
    }

    private fun setupTracks() {

        val clickListener: (Track) -> Unit = { track ->
            val params = bundleOf("parcel_track_key" to track)
            findNavController().navigate(R.id.action_playlistDetailFragment_to_audioPlayerFragment, params)
        }

        val longClickListener: (Track) -> Unit = { track ->
            showDeleteTrackDialog(track)
        }

        adapter = SearchAdapter(
            onClickListener = clickListener,
            onLongClickListener = longClickListener
        )
        binding.tracks.adapter = adapter
        binding.tracks.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showDeleteTrackDialog(track: Track) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_track))
            .setMessage(getString(R.string.delete_track_description))
            .setPositiveButton(getString(R.string.delete)) { _, _ -> viewModel.deleteTrack(track = track) }
            .setNegativeButton(getString(R.string.negative_button)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun showDeletePlaylistDialog(playlistName: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.delete_playlist_dialog, playlistName))
            .setPositiveButton(getString(R.string.yes)) { _, _ -> viewModel.deletePlayList() }
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}