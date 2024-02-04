package com.example.playlistmaker.main.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.playlistmaker.R
import com.example.playlistmaker.application.App
import com.example.playlistmaker.data.TrackToPlayListMediator
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.mapTheme.domain.GetMapThemeUseCase
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val getMapThemeUseCase: GetMapThemeUseCase by inject()
    private val trackToPlayListMediator: TrackToPlayListMediator by inject()
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as App).switchTheme(getMapThemeUseCase().isDarkTheme)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController


        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent))
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newPlaylistFragment, R.id.audioPlayerFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.horizontalLine.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.horizontalLine.visibility = View.VISIBLE
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                trackToPlayListMediator.observe()
                    .filter { it != null }
                    .collect {
                    navController.navigate(R.id.newPlaylistFragment)
                }
            }
        }

    }
}