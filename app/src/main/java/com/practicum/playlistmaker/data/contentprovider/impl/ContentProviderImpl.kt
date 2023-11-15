package com.practicum.playlistmaker.data.contentprovider.impl

import android.annotation.SuppressLint
import android.content.Context
import com.practicum.playlistmaker.data.contentprovider.ContentProvider

class ContentProviderImpl(private val context: Context) : ContentProvider {
    @SuppressLint("DiscouragedApi")
    override fun getStringFromResources(resourceString: String): String {
        val resourceId = context.resources
            .getIdentifier(resourceString, "string", context.packageName)
        return context.getString(resourceId)
    }
}