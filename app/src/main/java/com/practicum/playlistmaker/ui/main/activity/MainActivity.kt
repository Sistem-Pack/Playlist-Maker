package com.practicum.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.ui.mediatech.activity.MediatechActivity
import com.practicum.playlistmaker.ui.search.activity.SearchActivity
import com.practicum.playlistmaker.ui.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            Intent().apply {
                SearchActivity::class.java
                startActivity(this)
            }
        }

        binding.mediatechButton.setOnClickListener {
            Intent().apply {
                MediatechActivity::class.java
                startActivity(this)
            }
        }

        binding.settingsButton.setOnClickListener {
            Intent().apply {
                SettingsActivity::class.java
                startActivity(this)
            }
        }

    }
}