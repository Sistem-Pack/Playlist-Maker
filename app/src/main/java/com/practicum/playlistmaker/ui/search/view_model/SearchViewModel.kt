package com.practicum.playlistmaker.ui.search.view_model

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.domain.search.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: TrackSearchInteractor,
) : ViewModel() {

    private val tracksHistory = ArrayList<Track>()
    private var searchJob: Job? = null
    //private lateinit var searchRunnable: Runnable
    private var isClickAllowed = true

    //private val handler = Handler(Looper.getMainLooper())

    private var _searchScreenState = MutableLiveData<SearchState>()
    var searchScreenState: LiveData<SearchState> = _searchScreenState
    private var lastSearchRequest: String? = null

    init {
        tracksHistory.addAll(searchInteractor.readSearchHistory())
    }

    fun searchDebounce(changedText: String) {
        /*if (this::searchRunnable.isInitialized) {
            handler.removeCallbacks(searchRunnable)
        }
        searchRunnable = Runnable { searchRequest(changedText) }
        handler.postDelayed(searchRunnable, Consts.SEARCH_DEBOUNCE_DELAY)*/
        if (lastSearchRequest == changedText) return
        this.lastSearchRequest = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(Consts.SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showHistoryTracks() {
        tracksHistory.clear()
        tracksHistory.addAll(searchInteractor.readSearchHistory())
        if (tracksHistory.isNotEmpty()) _searchScreenState.value =
            SearchState.SearchHistory(tracksHistory)
        else _searchScreenState.value = SearchState.AllGone
    }

    fun clearSearchHistory() {
        tracksHistory.clear()
        searchInteractor.clearSearchHistory()
        if (tracksHistory.isNotEmpty()) _searchScreenState.value =
            SearchState.SearchHistory(tracksHistory)
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

            viewModelScope.launch {
                /*searchInteractor.search(searchText, object : TrackSearchInteractor.TracksConsumer {*/
                searchInteractor.search(searchText).collect { pair ->
                    processResult(pair.first, pair.second)
                }
                /*@SuppressLint("NotifyDataSetChanged")
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
                })*/
            }
        }

    }

    private fun processResult(foundTracks: ArrayList<Track>?, errorMessage: String?) {
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

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            searchJob = viewModelScope.launch {
                delay(Consts.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
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
        super.onCleared()
        searchInteractor.saveHistory(tracksHistory)
    /*//if (this::searchRunnable.isInitialized) {
            //handler.removeCallbacks(searchRunnable)
        }*/
    }
}