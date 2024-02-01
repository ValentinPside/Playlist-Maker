package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.extension.DateUtils
import com.example.playlistmaker.extension.visibleOrInvisible
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.PARCEL_TRACK_KEY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerFragment: Fragment(R.layout.activity_audio_player) {
    private lateinit var albumCover: ImageView
    private lateinit var bigTrackName: TextView
    private lateinit var bigBandName: TextView
    private lateinit var bigTrackTime: TextView
    private lateinit var lilTrackTime: TextView
    private lateinit var lilAlbumName: TextView
    private lateinit var lilReleaseDate: TextView
    private lateinit var lilPrimaryGenreName: TextView
    private lateinit var lilCountry: TextView
    private lateinit var btPlay: ImageView
    private lateinit var btPause: ImageView
    private lateinit var url: String
    private lateinit var favoriteButton:ImageView
    private lateinit var audioPlayerAddTrack:ImageView
    private val binding by viewBinding(ActivityAudioPlayerBinding::bind)
    private lateinit var playListAdapter: PlayListPreviewAdapter

    val track:Track by lazy { requireNotNull(requireArguments().getParcelable(PARCEL_TRACK_KEY)) }
    private val viewModel: AudioPlayerViewModel by viewModel { parametersOf(track) }
    private fun pausePlayer() {
        viewModel.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupPlayLists()
        btPlay.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
        btPause.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        binding.audioPlayerLikeTrack.setOnClickListener { viewModel.onFavoriteClick() }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observe().collect {state ->
                    updateInfo(state.track)

                    if (state.playerState.buttonText == "PAUSE") {
                        btPlay.visibility = View.INVISIBLE
                        btPause.visibility = View.VISIBLE
                    } else {
                        btPlay.visibility = View.VISIBLE
                        btPause.visibility = View.INVISIBLE
                    }

                    btPlay.isEnabled = state.playerState.isPlayButtonEnabled
                    bigTrackTime.text = state.playerState.progress

                    updateLikeIcon(state.isFavorite)
                    playListAdapter.submitList(state.playLists)

                    state.error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        viewModel.clearError()
                    }
                }
            }
        }

        val container = binding.standardBottomSheet
        val behavior = BottomSheetBehavior.from(container).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.audioPlayerAddTrack.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(p0: View, newState: Int) {
                binding.overlay.visibleOrInvisible(newState == BottomSheetBehavior.STATE_COLLAPSED)
            }
            override fun onSlide(p0: View, p1: Float) {}
        })
    }

    private fun setupPlayLists() {
        playListAdapter = PlayListPreviewAdapter() {
            viewModel.addTrackToPlayList(it)
        }
        binding.playListRecyclerView.adapter = playListAdapter
        binding.playListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun updateLikeIcon(isFavorite: Boolean) {
        val likeIcon = if (isFavorite) {
            R.drawable.remove_from_favorites
        } else {
            R.drawable.add_to_favorites
        }

        binding.audioPlayerLikeTrack.setImageDrawable(ContextCompat.getDrawable(requireContext(), likeIcon))

    }

    private fun initViews() {
        albumCover = binding.audioplayerAlbumCover
        bigTrackName = binding.audioplayerTrackName
        bigBandName = binding.audioplayerBandName
        bigTrackTime = binding.actualTrackTime
        lilTrackTime = binding.trackTimeRight
        lilAlbumName = binding.collectionNameRight
        lilReleaseDate = binding.releaseDateRight
        lilPrimaryGenreName = binding.primaryGenreNameRight
        lilCountry = binding.trackCountryRight
        btPlay = binding.audioPlayerPlayBut
        btPause = binding.audioPlayerPauseBut
        favoriteButton = binding.audioPlayerLikeTrack
        audioPlayerAddTrack = binding.audioPlayerAddTrack
        binding.newPlaylist.setOnClickListener {
            viewModel.createNewPlayList()
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    private fun updateInfo(track: Track) {
        Glide.with(requireContext())
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.bigplaceholder)
            .centerCrop().transform(
                RoundedCorners(
                    requireContext().resources.getDimensionPixelSize(
                        R.dimen.big_corner_radius
                    )
                )
            )
            .into(albumCover)

        bigTrackName.text = track.trackName
        bigBandName.text = track.artistName
        lilTrackTime.text = track.trackTimeMillis?.let { DateUtils.formatTime(it) }
        lilAlbumName.text = track.collectionName
        lilReleaseDate.text = track.releaseDate?.let { DateUtils.formatDate(it) }
        lilPrimaryGenreName.text = track.primaryGenreName
        lilCountry.text = track.country
        url = track.previewUrl.toString()
    }

}

