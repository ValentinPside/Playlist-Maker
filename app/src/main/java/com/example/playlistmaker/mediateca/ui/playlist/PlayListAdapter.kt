package com.example.playlistmaker.mediateca.ui.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayListItemBinding
import com.example.playlistmaker.domain.PlayList
import com.example.playlistmaker.uttils.StringUtils

class PlayListAdapter(
    private val onClick: (PlayList) -> Unit
): ListAdapter<PlayList, PlayListAdapter.ViewHolder>(DiffUtil()) {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding by viewBinding { PlayListItemBinding.bind(view) }

        fun bind(item: PlayList) {
            val transform = RequestOptions().transform(CenterCrop(), RoundedCorners(16))
            Glide.with(itemView.context)
                .load(item.uri)
                .placeholder(R.drawable.placeholder)
                .apply(transform)
                .into(binding.image)

            binding.name.text = item.name
            binding.tracks.text = "${item.tracks.size} ${StringUtils.getTracksAddition(item.tracks.size)}"
            binding.root.setOnClickListener { onClick.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.play_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

}

class DiffUtil(): DiffUtil.ItemCallback<PlayList>() {

    override fun areItemsTheSame(oldItem: PlayList, newItem: PlayList): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PlayList, newItem: PlayList): Boolean = oldItem == newItem

}