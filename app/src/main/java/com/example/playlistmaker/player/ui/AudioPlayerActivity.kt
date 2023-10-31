package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.extension.DateUtils
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.PARCEL_TRACK_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity: AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val PLAY_DEBOUNCE_DELAY = 400L
    }

    private var playerState = STATE_DEFAULT

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
    private var currentTextTime: String = "00:00"
    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            showPlay()
            playerState = STATE_PREPARED
            currentTextTime = "00:00"
            bigTrackTime.text = currentTextTime
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        showPause()
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        showPlay()
        playerState = STATE_PAUSED
        handler.removeCallbacks(playRunnable())
    }

    private fun playRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                if(playerState == STATE_PLAYING){
                    currentTextTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    bigTrackTime.text = currentTextTime
                    handler.postDelayed(this, PLAY_DEBOUNCE_DELAY)
                }
            }
        }
    }

    private fun playDebounce() {
        handler.post(playRunnable())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val track = requireNotNull(intent.extras?.getParcelable<Track>(PARCEL_TRACK_KEY))

        val playerViewModel: AudioPlayerViewModel by viewModel {
            parametersOf(track)
        }

        initViews()

        btPlay.setOnClickListener {
            startPlayer()
            playDebounce()
        }
        btPause.setOnClickListener {
            pausePlayer()
        }

        playerViewModel.observe().observe(this) {
            updateInfo(it.track)
            preparePlayer()
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

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(playRunnable())
    }

    private fun showPause(){
        btPlay.visibility = View.GONE
        btPause.visibility = View.VISIBLE
    }

    private fun showPlay(){
        btPause.visibility = View.GONE
        btPlay.visibility = View.VISIBLE
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
        bigTrackTime.text = currentTextTime
        lilTrackTime.text = track.trackTimeMillis?.let { DateUtils.formatTime(it) }
        lilAlbumName.text = track.collectionName
        lilReleaseDate.text = track.releaseDate?.let { DateUtils.formatDate(it) }
        lilPrimaryGenreName.text = track.primaryGenreName
        lilCountry.text = track.country
        url = track.previewUrl.toString()
    }

}

