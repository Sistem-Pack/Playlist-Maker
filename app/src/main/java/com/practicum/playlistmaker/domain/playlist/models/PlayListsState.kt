package com.practicum.playlistmaker.domain.playlist.models

import com.practicum.playlistmaker.domain.search.models.PlayList

sealed interface PlayListsState {

    object Empty : PlayListsState

    data class AddTrackToPlayListResult(
        val isAdded: Boolean,
        val playListName: String
    ) : PlayListsState

    data class PlayLists(
        val playLists: List<PlayList>
    ) : PlayListsState

}