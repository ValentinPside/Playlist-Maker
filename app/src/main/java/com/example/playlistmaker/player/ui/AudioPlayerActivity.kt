package com.example.playlistmaker.player.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.extension.DateUtils
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.PARCEL_TRACK_KEY
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerActivity: AppCompatActivity() {

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

    val track by lazy { requireNotNull(intent.extras?.getParcelable<Track>(PARCEL_TRACK_KEY)) }
    private val viewModel: AudioPlayerViewModel by viewModel { parametersOf(track) }

    private fun pausePlayer() {
        viewModel.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        initViews()

        btPlay.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
        btPause.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observe().collect {
                    updateInfo(it.track)

                    if (it.playerState.buttonText == "PAUSE") {
                        btPlay.visibility = View.GONE
                        btPause.visibility = View.VISIBLE
                    } else {
                        btPlay.visibility = View.VISIBLE
                        btPause.visibility = View.GONE
                    }

                    btPlay.isEnabled = it.playerState.isPlayButtonEnabled
                    bigTrackTime.text = it.playerState.progress
                }
            }
        }

    }

    private fun initViews() {
        albumCover = findViewById(R.id.audioplayer_album_cover)
        bigTrackName = findViewById(R.id.audioplayer_track_name)
        bigBandName = findViewById(R.id.audioplayer_band_name)
        bigTrackTime = findViewById(R.id.actualTrackTime)
        lilTrackTime = findViewById(R.id.track_timeRight)
        lilAlbumName = findViewById(R.id.collectionNameRight)
        lilReleaseDate = findViewById(R.id.releaseDateRight)
        lilPrimaryGenreName = findViewById(R.id.primaryGenreNameRight)
        lilCountry = findViewById(R.id.trackCountryRight)
        btPlay = findViewById(R.id.audioPlayerPlayBut)
        btPause = findViewById(R.id.audioPlayerPauseBut)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    private fun updateInfo(track: Track) {
        Glide.with(applicationContext)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.bigplaceholder)
            .centerCrop().transform(
                RoundedCorners(
                    applicationContext.resources.getDimensionPixelSize(
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

