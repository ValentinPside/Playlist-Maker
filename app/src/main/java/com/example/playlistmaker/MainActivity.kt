package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

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
            navigateTo(Mediateca::class.java)
        }
        searchButton.setOnClickListener {
            navigateTo(Search::class.java)
        }
        settingsButton.setOnClickListener {
            navigateTo(Settings::class.java)
        }
    }
}