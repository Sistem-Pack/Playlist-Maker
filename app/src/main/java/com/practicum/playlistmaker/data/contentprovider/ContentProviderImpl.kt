package com.practicum.playlistmaker.data.contentprovider

import android.annotation.SuppressLint
import android.content.Context
import com.practicum.playlistmaker.domain.contentprovider.ContentProvider

class ContentProviderImpl(private val context: Context) : ContentProvider {
    @SuppressLint("DiscouragedApi")
    override fun getStringFromResources(value: String): String {
        val resourceId = context.resources
            .getIdentifier(value, "string", context.packageName)
        return context.getString(resourceId)
    }

}