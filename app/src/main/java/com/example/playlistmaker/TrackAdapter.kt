package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter () : RecyclerView.Adapter<TrackViewHolder> () {

    var tracks = ArrayList<Track>()
    private val searchHistoryObj = SearchHistory()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        App.getSharedPreferences()
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            searchHistoryObj.editArray(tracks[position])
    }
}


override fun getItemCount(): Int =  tracks.size

}