package com.practicum.playlistmaker.ui.search.view_model

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.creator.Consts
import com.practicum.playlistmaker.data.contentprovider.ContentProvider
import com.practicum.playlistmaker.data.search.ShowPlayerInteractor
import com.practicum.playlistmaker.domain.search.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.search.models.SearchHistory
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity
import com.practicum.playlistmaker.ui.track.TrackAdapter

class SearchViewModel(
    private val searchInteractor: TrackSearchInteractor,
    private val contentProvider: ContentProvider,
    private val showPlayerInteractor: ShowPlayerInteractor,
) : ViewModel() {

    private val tracksHistory = ArrayList<Track>()
    private lateinit var adapterTracks: TrackAdapter
    private lateinit var adapterTracksHistory: TrackAdapter
    private lateinit var searchRunnable: Runnable

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val handlerClickDebounce = Handler(Looper.getMainLooper())

    private var _searchScreenState = MutableLiveData<SearchState>()
    var searchScreenState: LiveData<SearchState> = _searchScreenState

    init {
        tracksHistory.addAll(searchInteractor.readSearchHistory())
    }

    fun setTrackAdapters(trackRecyclerView: RecyclerView, historyTrackRecyclerView: RecyclerView) {
        adapterTracks = TrackAdapter() {
            addTrackToSearchHistory(it)
            intentAudioPlayer(it)
        }
        adapterTracksHistory = TrackAdapter() {
            intentAudioPlayer(it)
        }
        trackRecyclerView.adapter = adapterTracks
        historyTrackRecyclerView.adapter = adapterTracksHistory
    }

    fun searchDebounce(changedText: String) {
        searchRunnable = Runnable { searchRequest(changedText) }
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, Consts.SEARCH_DEBOUNCE_DELAY)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showHistoryTracks() {
        tracksHistory.clear()
        tracksHistory.addAll(searchInteractor.readSearchHistory())
        adapterTracksHistory.setTracks(tracksHistory)
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
                                SearchState.Empty(
                                    emptyMessage = contentProvider.getStringFromResources("share_link")
                                )
                            )
                        }

                        else -> {
                            adapterTracks.setTracks(foundTracks)
                            _searchScreenState.postValue(
                                SearchState.Content
                            )
                        }
                    }
                }

            })
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handlerClickDebounce.postDelayed({ isClickAllowed = true }, Consts.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun intentAudioPlayer(track: Track) {
        if (clickDebounce()) {
            showPlayerInteractor.openPlayer(track)
        }
    }

    private fun addTrackToSearchHistory(track: Track) {
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
        handler.removeCallbacks(searchRunnable)
    }
}