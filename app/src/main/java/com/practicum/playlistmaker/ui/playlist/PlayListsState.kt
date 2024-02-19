package com.practicum.playlistmaker.ui.playlist

import com.practicum.playlistmaker.domain.search.models.PlayList

sealed interface PlayListsState {

    object Empty : PlayListsState

    data class PlayLists(
        val tracks: List<PlayList>
    ) : PlayListsState
}