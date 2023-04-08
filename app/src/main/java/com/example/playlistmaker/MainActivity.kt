package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mediatecaButton = findViewById<Button>(R.id.button2)
        val searchButton = findViewById<Button>(R.id.button1)
        val settingsButton = findViewById<Button>(R.id.button3)

        mediatecaButton.setOnClickListener {
            val displayIntent = Intent(this, Mediateca::class.java)
            startActivity(displayIntent)
        }
        searchButton.setOnClickListener {
            val displayIntent = Intent(this, Search::class.java)
            startActivity(displayIntent)
        }
        settingsButton.setOnClickListener {
            val displayIntent = Intent(this, Settings::class.java)
            startActivity(displayIntent)
        }
    }
}