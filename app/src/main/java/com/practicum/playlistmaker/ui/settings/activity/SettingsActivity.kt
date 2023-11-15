package com.practicum.playlistmaker.ui.settings.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModelFactory

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(this, application)
        )[SettingsViewModel::class.java]

        binding.modeSwitch.isChecked = (applicationContext as App).darkTheme

        binding.modeSwitch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.offer.setOnClickListener {
            settingsViewModel.openOffer()
        }

        binding.send.setOnClickListener {
            settingsViewModel.sendMail()
        }

        binding.share.setOnClickListener {
            settingsViewModel.shareApp()
        }
    }


}