package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayListPreviewItemBinding
import com.example.playlistmaker.domain.PlayList
import com.example.playlistmaker.uttils.StringUtils

class PlayListPreviewAdapter(
    private val onClickPlayList: (Int) -> Unit
): ListAdapter<PlayList, PlayListPreviewAdapter.ViewHolder>(DiffUtil()) {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding by viewBinding { PlayListPreviewItemBinding.bind(itemView) }

        fun bind(item: PlayList) {
            Glide.with(itemView.context)
                .load(item.uri)
                .placeholder(R.drawable.placeholder)
                .into(binding.image)

            binding.title.text = item.name
            binding.count.text = "${item.tracks.size} ${StringUtils.getTracksAddition(item.tracks.size)}"
            binding.root.setOnClickListener { onClickPlayList.invoke(item.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.play_list_preview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

}

class DiffUtil: DiffUtil.ItemCallback<PlayList>() {
    override fun areItemsTheSame(oldItem: PlayList, newItem: PlayList): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PlayList, newItem: PlayList): Boolean {
        return oldItem == newItem
    }

}