package com.example.playlistmaker.search.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

class SearchViewHolder (parentView: View) : RecyclerView.ViewHolder(parentView) {
    val trackName: TextView = itemView.findViewById(R.id.track_name)
    val artistName: TextView = itemView.findViewById(R.id.band_name)
    val trackTime: TextView = itemView.findViewById(R.id.track_time)
    val albumsCover: ImageView = itemView.findViewById(R.id.albums_cover)

}
