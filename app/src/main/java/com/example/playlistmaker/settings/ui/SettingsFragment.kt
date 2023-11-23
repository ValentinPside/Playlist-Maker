package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.Application.App
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shareView = binding.share
        val supportView = binding.support
        val forwardView = binding.forward
        val themeSwitcher = binding.switch2

        settingsViewModel.observeViewState().observe(viewLifecycleOwner) { it ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        (requireActivity().application as App).switchTheme(isDarkTheme)
    }
}