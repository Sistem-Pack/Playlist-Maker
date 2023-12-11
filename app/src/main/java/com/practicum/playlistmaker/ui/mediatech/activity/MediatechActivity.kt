package com.practicum.playlistmaker.ui.mediatech.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediatechBinding

class MediatechActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediatechBinding
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatechBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.title_activity_mediatech)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)

            binding?.viewPager?.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

            tabMediator =
                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    when (position) {
                        0 -> tab.text = getString(R.string.media_favorite_tracks_title)
                        1 -> tab.text = getString(R.string.media_playlist_title)
                    }
                }
            tabMediator?.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
    }
}


