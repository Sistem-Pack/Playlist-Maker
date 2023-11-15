package com.practicum.playlistmaker.data.contentprovider

interface ContentProvider {
    fun getStringFromResources(resourceString: String): String
}