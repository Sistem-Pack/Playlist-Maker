package com.practicum.playlistmaker.ui.search.view_model

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.domain.search.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.search.models.Track

class SearchViewModel(
    private val searchInteractor: TrackSearchInteractor,
) : ViewModel() {

    private val tracksHistory = ArrayList<Track>()
    private lateinit var searchRunnable: Runnable

    private val handler = Handler(Looper.getMainLooper())

    private var _searchScreenState = MutableLiveData<SearchState>()
    var searchScreenState: LiveData<SearchState> = _searchScreenState

    init {
        tracksHistory.addAll(searchInteractor.readSearchHistory())
    }

    fun searchDebounce(changedText: String) {
        if (this::searchRunnable.isInitialized) {
            handler.removeCallbacks(searchRunnable)
        }
        searchRunnable = Runnable { searchRequest(changedText) }
        handler.postDelayed(searchRunnable, Consts.SEARCH_DEBOUNCE_DELAY)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showHistoryTracks() {
        tracksHistory.clear()
        tracksHistory.addAll(searchInteractor.readSearchHistory())
        if (tracksHistory.isNotEmpty()) _searchScreenState.value = SearchState.SearchHistory(tracksHistory)
        else _searchScreenState.value = SearchState.AllGone
    }

    fun clearSearchHistory() {
        tracksHistory.clear()
        searchInteractor.clearSearchHistory()
        if (tracksHistory.isNotEmpty()) _searchScreenState.value = SearchState.SearchHistory(tracksHistory)
        else _searchScreenState.value = SearchState.AllGone
    }

    fun searchFocusChanged(hasFocus: Boolean, text: String) {
        if (hasFocus && text.isEmpty()) {
            if (tracksHistory.isNotEmpty()) {
                _searchScreenState.value = SearchState.SearchHistory(tracksHistory)
            } else _searchScreenState.value = SearchState.AllGone
        }
    }

    private fun searchRequest(searchText: String) {
        if (searchText.isNotEmpty()) {
            _searchScreenState.value = SearchState.Loading

            searchInteractor.search(searchText, object : TrackSearchInteractor.TracksConsumer {
                @SuppressLint("NotifyDataSetChanged")
                override fun consume(foundTracks: ArrayList<Track>?, errorMessage: String?) {

                    when {
                        errorMessage != null -> {
                            _searchScreenState.postValue(
                                SearchState.Error(
                                    errorMessage = errorMessage
                                )
                            )
                        }

                        foundTracks.isNullOrEmpty() -> {
                            _searchScreenState.postValue(
                                SearchState.Empty
                            )
                        }

                        else -> {
                            _searchScreenState.postValue(
                                SearchState.Content(foundTracks)
                            )
                        }
                    }
                }

            })
        }
    }

    fun addTrackToSearchHistory(track: Track) {
        if (tracksHistory.contains(track)) {
            tracksHistory.remove(track)
        }
        tracksHistory.add(0, track)
        if (tracksHistory.size > Consts.MAX_TRACKS_IN_HISTORY) {
            tracksHistory.removeLast()
        }
        searchInteractor.saveHistory(tracksHistory)
    }

    override fun onCleared() {
        if (this::searchRunnable.isInitialized) {
            handler.removeCallbacks(searchRunnable)
        }
    }
}