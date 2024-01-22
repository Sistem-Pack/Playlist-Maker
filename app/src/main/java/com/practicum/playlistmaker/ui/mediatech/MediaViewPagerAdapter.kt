package com.practicum.playlistmaker.ui.mediatech

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.Consts.MEDIA_VIEW_PAGER_ADAPTER_ITEM_COUNT
import com.practicum.playlistmaker.ui.mediatech.fragments.MediaFragmentFavoriteTracks
import com.practicum.playlistmaker.ui.mediatech.fragments.MediaFragmentPlaylists

class MediaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return MEDIA_VIEW_PAGER_ADAPTER_ITEM_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MediaFragmentFavoriteTracks.newInstance()
            else -> MediaFragmentPlaylists.newInstance()
        }
    }
}