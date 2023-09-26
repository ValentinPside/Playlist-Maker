package com.example.playlistmaker.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.R

class MainActivity : AppCompatActivity() {
    private fun navigateTo(clazz: Class<out AppCompatActivity>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mediatecaButton = findViewById<Button>(R.id.button2)
        val searchButton = findViewById<Button>(R.id.button1)
        val settingsButton = findViewById<Button>(R.id.button3)

        mediatecaButton.setOnClickListener {
            navigateTo(MediatecaActivity::class.java)
        }
        searchButton.setOnClickListener {
            navigateTo(SearchActivity::class.java)
        }
        settingsButton.setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }
    }
}