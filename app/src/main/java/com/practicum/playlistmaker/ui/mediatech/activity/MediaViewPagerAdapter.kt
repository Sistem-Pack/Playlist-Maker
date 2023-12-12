package com.practicum.playlistmaker.ui.mediatech.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.ui.mediatech.activity.fragments.MediaFragmentFavoriteTracks
import com.practicum.playlistmaker.ui.mediatech.activity.fragments.MediaFragmentPlaylists

class MediaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MediaFragmentFavoriteTracks.newInstance()
            else -> MediaFragmentPlaylists.newInstance()
        }
    }
}