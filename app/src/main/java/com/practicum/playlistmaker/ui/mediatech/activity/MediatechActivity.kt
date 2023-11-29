package com.practicum.playlistmaker.ui.mediatech.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMediatechBinding

class MediatechActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediatechBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatechBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}