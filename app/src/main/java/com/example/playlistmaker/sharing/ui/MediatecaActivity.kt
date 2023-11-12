package com.example.playlistmaker.sharing.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatecaBinding
import com.example.playlistmaker.sharing.data.MediatecaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediatecaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediatecaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatecaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener { onBackPressed() }

        binding.viewPager.adapter = MediatecaViewPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.mediateca_first_fragment_name)
                1 -> tab.text = getString(R.string.mediateca_second_fragment_name)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}