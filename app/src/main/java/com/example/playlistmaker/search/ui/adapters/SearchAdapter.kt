package com.example.playlistmaker.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.extension.DateUtils
import com.example.playlistmaker.search.domain.models.Track

class SearchAdapter(
    var tracks : List<Track> = emptyList(),
    private val onClickListener: (Track) -> Unit,
    private val onLongClickListener: ((Track) -> Unit)? = null
    ): RecyclerView.Adapter<SearchViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val itemView = holder.itemView
        val track = tracks[position]

        itemView.setOnClickListener {
            onClickListener.invoke(track)
        }
        onLongClickListener?.let {callback ->
            itemView.setOnLongClickListener {
                callback.invoke(track)
                true
            }
        }

        holder.trackName.text = track.trackName
        holder.artistName.text = track.artistName
        holder.trackTime.text = track.trackTimeMillis?.let { DateUtils.formatTime(it) }

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(
                    itemView.resources.getDimensionPixelSize(
                        R.dimen.corner_radius
                    )
                )
            )
            .into(holder.albumsCover)
    }


    override fun getItemCount(): Int {
        return tracks.size
    }
}