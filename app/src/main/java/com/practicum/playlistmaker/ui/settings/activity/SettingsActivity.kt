package com.practicum.playlistmaker.ui.settings.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val settingsViewModel by viewModel<SettingsViewModel>()

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsViewModel.themeSettingsState.observe(this) { themeSettings ->
            binding.modeSwitch.isChecked = themeSettings.darkTheme
        }

        binding.modeSwitch.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.switchTheme(checked)
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