package com.practicum.playlistmaker.ui.search.view_model

import com.practicum.playlistmaker.domain.search.models.Track

sealed interface SearchState {
    data object Loading : SearchState
    data object Content : SearchState
    data class Error(val errorMessage: String) : SearchState
    data class SearchHistory(val tracks: List<Track>) : SearchState
    data class Empty(val emptyMessage: String) : SearchState
}
