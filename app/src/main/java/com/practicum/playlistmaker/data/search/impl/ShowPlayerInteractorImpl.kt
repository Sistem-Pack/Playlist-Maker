package com.practicum.playlistmaker.data.search.impl

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.data.contentprovider.ContentProvider
import com.practicum.playlistmaker.data.search.ShowPlayerInteractor
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity

class ShowPlayerInteractorImpl(private val context: Context, private val contentProvider: ContentProvider): ShowPlayerInteractor {
    override fun openPlayer(track: Track) {
        Intent(context, PlayerActivity::class.java).apply {
            putExtra(contentProvider.getStringFromResources("track"), track)
            context.startActivity(this)
        }
    }

}