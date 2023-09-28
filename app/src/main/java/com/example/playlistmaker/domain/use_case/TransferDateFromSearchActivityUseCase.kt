package com.example.playlistmaker.domain.use_case

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.PARCEL_TRACK_KEY

class TransferDateFromSearchActivityUseCase {
     fun execute(intent: Intent, applicationContext : Context, albumCover : ImageView) : Track? {
        var data = intent.extras
        var track = data?.getParcelable<Track>(PARCEL_TRACK_KEY)

        if (track != null) {
            Glide.with(applicationContext)
                .load(track.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg"))
                .placeholder(R.drawable.bigplaceholder)
                .centerCrop().transform(
                    RoundedCorners(
                        applicationContext.resources.getDimensionPixelSize(
                            R.dimen.big_corner_radius
                        )
                    )
                )
                .into(albumCover)
        }
        return track
    }
}