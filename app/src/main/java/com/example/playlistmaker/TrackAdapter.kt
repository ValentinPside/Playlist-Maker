package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter (var tracks: List<Track>) : RecyclerView.Adapter<TrackViewHolder> () {
    private val searchHistoryObj = SearchHistory()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view as ViewGroup)
    }


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        App.getSharedPreferences()
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            searchHistoryObj.editArray(tracks[position])
    }
}


override fun getItemCount(): Int =  tracks.size

}