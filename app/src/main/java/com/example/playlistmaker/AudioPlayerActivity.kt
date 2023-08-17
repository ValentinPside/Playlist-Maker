package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var albumCover: ImageView
    private lateinit var bigTrackName: TextView
    private lateinit var bigBandName: TextView
    private lateinit var bigTrackTime: TextView
    private lateinit var lilTrackTime: TextView
    private lateinit var lilAlbumName: TextView
    private lateinit var lilReleaseDate: TextView
    private lateinit var lilPrimaryGenreName: TextView
    private lateinit var lilCountry: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        albumCover = findViewById(R.id.audioplayer_album_cover)
        bigTrackName = findViewById(R.id.audioplayer_track_name)
        bigBandName = findViewById(R.id.audioplayer_band_name)
        bigTrackTime = findViewById(R.id.actualTrackTime)
        lilTrackTime = findViewById(R.id.track_timeRight)
        lilAlbumName = findViewById(R.id.collectionNameRight)
        lilReleaseDate = findViewById(R.id.releaseDateRight)
        lilPrimaryGenreName = findViewById(R.id.primaryGenreNameRight)
        lilCountry = findViewById(R.id.trackCountryRight)

        transferDateFromSearchActivity()

    }
    private fun transferDateFromSearchActivity() {
        var arguments: Bundle? = intent.extras

        Glide.with(applicationContext)
            .load(arguments?.getString("album cover"))
            .placeholder(R.drawable.bigplaceholder)
            .centerCrop().transform(
                RoundedCorners(
                    applicationContext.resources.getDimensionPixelSize(
                    R.dimen.big_corner_radius
                )
            )
            )
            .into(albumCover)
        bigTrackName.text = arguments?.getString("name song")
        bigBandName.text = arguments?.getString("band")
        bigTrackTime.text = arguments?.getLong("duration")?.let { DateUtils.formatTime(it) }
        lilTrackTime.text = arguments?.getLong("duration")?.let { DateUtils.formatTime(it) }
        lilAlbumName.text = arguments?.getString("album")
        lilReleaseDate.text = arguments?.getString("year")
        lilPrimaryGenreName.text = arguments?.getString("genre")
        lilCountry.text = arguments?.getString("country")
    }
}

