package com.practicum.playlistmaker.ui.mediatech.play

import com.practicum.playlistmaker.domain.search.models.PlayList

sealed interface PlayListsState {

    data object Empty : PlayListsState

    data class PlayLists(
        val tracks: List<PlayList>
    ) : PlayListsState
}