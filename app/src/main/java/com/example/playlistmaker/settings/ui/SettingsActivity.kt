package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.R
import com.example.playlistmaker.Application.App
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val shareView = findViewById<ImageView>(R.id.share)
        val supportView = findViewById<ImageView>(R.id.support)
        val forwardView = findViewById<ImageView>(R.id.forward)
        val themeSwitcher = findViewById<SwitchCompat>(R.id.switch2)

        settingsViewModel.observeViewState().observe(this) {
            subscribeSwitchTheme(it.switchTheme)
            themeSwitcher.isChecked = it.switchTheme

            it.action?.let {
                when (it) {
                    ActionView.SEND_EMAIL -> { subscribeOnSendEmail() }
                    ActionView.OPEN_PRACTICUM_LINK -> { subscribeOpenPracticumLink() }
                    ActionView.OPEN_OFFER_LINK -> { subscribeOpenOfferLink() }
                }
            }
        }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.switchTheme(checked)
        }

        shareView.setOnClickListener {
            settingsViewModel.openPracticumLink()
        }

        supportView.setOnClickListener {
            settingsViewModel.sendEmail()
        }

        forwardView.setOnClickListener {
            settingsViewModel.openOfferLink()
        }
    }

    private fun subscribeOnSendEmail(){
            val subject = getString(R.string.SettingsSubject)
            val message = getString(R.string.SettingsSubText)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("valentinmalts@yandex.ru"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
    }

    private fun subscribeOpenPracticumLink(){
            Intent(Intent.ACTION_SEND).apply{type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
                Intent.createChooser(this, null)
                startActivity(this)
            }
    }

    private fun subscribeOpenOfferLink(){
            Intent(Intent.ACTION_VIEW).apply{
                data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
                startActivity(this)
            }
    }

    private fun subscribeSwitchTheme(isDarkTheme: Boolean){
        (application as App).switchTheme(isDarkTheme)
    }
}