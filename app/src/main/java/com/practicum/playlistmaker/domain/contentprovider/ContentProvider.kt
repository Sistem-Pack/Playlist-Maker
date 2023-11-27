package com.practicum.playlistmaker.domain.contentprovider

import android.graphics.drawable.Drawable

interface ContentProvider {
    fun getStringFromResources(resourceString: String): String
    fun getDrawableFromResources(resourceString: String): Drawable?

}