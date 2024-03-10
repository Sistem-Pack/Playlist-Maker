package com.practicum.playlistmaker.ui.mediatech.play

import com.practicum.playlistmaker.domain.search.models.PlayList

sealed interface PlayListsState {

    data object Empty : PlayListsState

    data class AddTrackToPlayListResult(
        val isAdded: Boolean,
        val playListName: String
    ) : PlayListsState

    data class PlayLists(
        val playLists: List<PlayList>
    ) : PlayListsState
}