package com.practicum.playlistmaker.data.contentprovider

import android.graphics.drawable.Drawable
import androidx.annotation.Dimension

interface ContentProvider {
    fun getStringFromResources(resourceString: String): String
    fun getDrawableFromResources(resourceString: String): Drawable?

}