package com.example.playlistmaker.sharing.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.MediatecaPlaylistsBinding

class PlayListsFragment : Fragment() {

    private lateinit var binding: MediatecaPlaylistsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MediatecaPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

}