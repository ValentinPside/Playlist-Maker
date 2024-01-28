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
import com.example.playlistmaker.R
import com.example.playlistmaker.application.App
import com.example.playlistmaker.data.TrackToPlayListMediator
import com.example.playlistmaker.mapTheme.domain.GetMapThemeUseCase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val getMapThemeUseCase: GetMapThemeUseCase by inject()
    private val trackToPlayListMediator: TrackToPlayListMediator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as App).switchTheme(getMapThemeUseCase().isDarkTheme)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent))
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newPlaylistFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
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