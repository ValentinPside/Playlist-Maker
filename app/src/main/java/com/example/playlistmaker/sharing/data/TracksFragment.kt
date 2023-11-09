package com.example.playlistmaker.sharing.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.MediatecaTracksBinding

class TracksFragment : Fragment() {

    private lateinit var binding: MediatecaTracksBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MediatecaTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

}