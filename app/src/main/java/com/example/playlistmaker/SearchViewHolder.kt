package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class SearchViewHolder (parentView: View) : RecyclerView.ViewHolder(parentView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.band_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val albumsCover: ImageView = itemView.findViewById(R.id.albums_cover)


    fun Long.formatTime(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime.formatTime().toString()
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(albumsCover)
    }
}
