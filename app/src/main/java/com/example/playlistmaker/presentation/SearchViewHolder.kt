package com.example.playlistmaker.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.DateUtils
import com.example.playlistmaker.domain.models.Track

class SearchViewHolder (parentView: View) : RecyclerView.ViewHolder(parentView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.band_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val albumsCover: ImageView = itemView.findViewById(R.id.albums_cover)

    fun bind(track: Track, listener: SearchAdapter.Listener) {
        itemView.setOnClickListener{
            listener.onClick(track)
        }
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTimeMillis?.let { DateUtils.formatTime(it) }
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(
                itemView.resources.getDimensionPixelSize(
                    R.dimen.corner_radius
                )
            ))
            .into(albumsCover)
    }
}
