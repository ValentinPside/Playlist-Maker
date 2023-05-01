package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val shareView = findViewById<ImageView>(R.id.share)
        val supportView = findViewById<ImageView>(R.id.support)
        val forwardView = findViewById<ImageView>(R.id.forward)
        shareView.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.YP_Android_URL)
            startActivity(Intent.createChooser(shareIntent, "Choose"))
        }
        supportView.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("valentinmalts@yandex.ru"))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.SettingsSubject)
            supportIntent.putExtra(Intent.EXTRA_TEXT, R.string.SettingsText)
            startActivity(supportIntent)
        }
        forwardView.setOnClickListener {
            val forwardIntent = Intent(Intent.ACTION_VIEW)
            forwardIntent.data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            startActivity(forwardIntent)
        }
    }
}