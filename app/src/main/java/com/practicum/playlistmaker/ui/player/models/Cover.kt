package com.practicum.playlistmaker.ui.player.models

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.contentprovider.ContentProvider

class Cover(private val context: Context, private val contentProvider: ContentProvider) {

    fun provideImage(imageURL: String?, imageView: ImageView) {
        Glide.with(context)
            .load(imageURL)
            .placeholder(contentProvider.getDrawableFromResources("album_3x"))
            .centerCrop()
            .transform(RoundedCorners(context.resources.getDimensionPixelSize(R.dimen.dm2)))
            .into(imageView)
    }
}