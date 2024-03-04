package com.practicum.playlistmaker.ui.player

import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.domain.search.models.Track

sealed interface PlayListState {
    data class PlayListInfo(val playList: PlayList) : PlayListState
    data class PlayListTracks(val tracks: List<Track>) : PlayListState
}