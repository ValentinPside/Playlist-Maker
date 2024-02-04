package com.example.playlistmaker.mediateca.data

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.mediateca.PlayListsFragment
import com.example.playlistmaker.mediateca.ui.tracks.TracksFragment

class MediatecaViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> TracksFragment.newInstance()
            else -> PlayListsFragment.newInstance()
        }
    }
}