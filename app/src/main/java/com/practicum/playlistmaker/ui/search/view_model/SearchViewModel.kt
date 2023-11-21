package com.practicum.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.creator.Consts
import com.practicum.playlistmaker.data.contentprovider.ContentProvider
import com.practicum.playlistmaker.domain.search.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.search.models.SearchHistory
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.track.TrackAdapter

class SearchViewModel(
    private val searchInteractor: TrackSearchInteractor,
    private val contentProvider: ContentProvider,
) : ViewModel() {

    private val tracks = ArrayList<Track>()
    private val tracksHistory = ArrayList<Track>()
    private val adapterTracks: TrackAdapter = TrackAdapter()
    private val adapterTracksHistory: TrackAdapter = TrackAdapter()
    private val searchRunnable = Runnable { searchRequest() }

    private val handler = Handler(Looper.getMainLooper())
    private var searchText: String = ""

    private var _searchScreenState = MutableLiveData<SearchState>()
    var searchScreenState: LiveData<SearchState> = _searchScreenState

    init {
        tracksHistory.addAll(searchInteractor.readSearchHistory())
    }

    fun setTrackAdapters(trackRecyclerView: RecyclerView, historyTrackRecyclerView: RecyclerView) {
        trackRecyclerView.adapter = adapterTracks
        historyTrackRecyclerView.adapter = adapterTracksHistory
    }

    fun searchDebounce(changedText: String) {
        searchText = changedText
        handler.removeCallbacksAndMessages(searchRunnable)
        handler.postDelayed(searchRunnable, Consts.SEARCH_DEBOUNCE_DELAY)
    }

    fun showHistoryTracks() {
        _searchScreenState.value = SearchState.SearchHistory(tracksHistory)
    }

    fun clearSearchHistory() {
        tracksHistory.clear()
        _searchScreenState.value = SearchState.SearchHistory(tracksHistory)
        searchInteractor.clearSearchHistory()
    }

    fun searchFocusChanged(hasFocus: Boolean, text: String) {
        if (hasFocus && text.isEmpty()) {
            _searchScreenState.value = SearchState.SearchHistory(tracksHistory)
        }
    }

    private fun searchRequest() {

        if (searchText.isNotEmpty()) {
            _searchScreenState.value = SearchState.Loading

            searchInteractor.search(searchText, object : TrackSearchInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>, errorMessage: String?) {
                    val tracks = mutableListOf<Track>()
                    tracks.addAll(foundTracks)

                    when {
                        errorMessage != null -> {
                            _searchScreenState.postValue(
                                SearchState.Error(
                                errorMessage = errorMessage)
                            )
                        }

                        tracks.isEmpty() -> {
                            _searchScreenState.postValue(
                                SearchState.Empty(
                                emptyMessage = contentProvider.getStringFromResources("share_link"))
                            )
                        }

                        else -> {
                            _searchScreenState.postValue(
                                SearchState.Content(
                                tracks = tracks)
                            )
                        }
                    }
                }

            })
        }
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(searchRunnable)
    }
}