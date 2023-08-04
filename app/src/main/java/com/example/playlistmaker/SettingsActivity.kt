package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val shareView = findViewById<ImageView>(R.id.share)
        val supportView = findViewById<ImageView>(R.id.support)
        val forwardView = findViewById<ImageView>(R.id.forward)
        val themeSwitcher = findViewById<Switch>(R.id.switch2)

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        shareView.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                val message = getString(R.string.YP_Android_URL)
                putExtra(Intent.EXTRA_TEXT, message)
                Intent.createChooser(this, null)
                startActivity(this)
            }
        }

        supportView.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                val subject = getString(R.string.SettingsSubject)
                val message = getString(R.string.SettingsSubText)
                putExtra(Intent.EXTRA_EMAIL, arrayOf("valentinmalts@yandex.ru"))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
                startActivity(this)
            }
        }

        forwardView.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply{
                data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
                startActivity(this)
            }
        }
    }
}