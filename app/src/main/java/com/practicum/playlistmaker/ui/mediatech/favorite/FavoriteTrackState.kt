package com.practicum.playlistmaker.ui.mediatech.favorite

import com.practicum.playlistmaker.domain.search.models.Track

sealed interface FavoriteTrackState {

    data object Loading : FavoriteTrackState

    data class Content(
        val tracks: List<Track>
    ) : FavoriteTrackState

    data object Empty : FavoriteTrackState
}