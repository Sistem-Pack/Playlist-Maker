package com.practicum.playlistmaker.domain.contentprovider

interface ContentProvider {
    fun getStringFromResources(resourceString: String): String
}