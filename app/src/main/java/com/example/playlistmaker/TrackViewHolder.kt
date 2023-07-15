package com.example.playlistmaker


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class TrackViewHolder(parent: ViewGroup):
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)) {

    private val albumsCover: ImageView = itemView.findViewById(R.id.albums_cover)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val bandName: TextView = itemView.findViewById(R.id.band_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    var trackNumber:Long = 0

    fun Long.formatTime(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)

    fun bind(item: Track) {
        trackName.text = item.trackName
        bandName.text = item.artistName
        trackTime.text = item.trackTimeMillis.formatTime()
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(albumsCover)
    }
}