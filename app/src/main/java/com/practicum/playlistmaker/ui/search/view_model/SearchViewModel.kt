package com.practicum.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Consts
import com.practicum.playlistmaker.data.contentprovider.ContentProvider
import com.practicum.playlistmaker.domain.search.api.TrackSearchInteractor
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.track.TrackAdapter

class SearchViewModel(
    private val searchInteractor: TrackSearchInteractor,
    private val contentProvider: ContentProvider,
) : ViewModel() {

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val tracks = ArrayList<Track>()
    private val tracksHistory = ArrayList<Track>()
    private val adapterSearch: TrackAdapter = TrackAdapter(tracks)
    private val adapterHistory: TrackAdapter = TrackAdapter(tracksHistory)
    private val searchRunnable = Runnable { searchRequest() }

    private val handler = Handler(Looper.getMainLooper())
    private var searchText: String = ""

    private var _searchScreenState = MutableLiveData<SearchState>()
    var searchScreenState: LiveData<SearchState> = _searchScreenState

    init {
        tracksHistory.addAll(searchInteractor.readSearchHistory())
    }

    fun searchDebounce(changedText: String) {
        searchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        handler.postDelayed(searchRunnable, Consts.SEARCH_DEBOUNCE_DELAY)
    }

    fun showHistoryTracks() {
        _searchScreenState.value = SearchState.SearchHistory(tracksHistory)
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


    /*private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchRequest(changedText) }
        val postTime = SystemClock.uptimeMillis() + Constants.SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }



    fun clearSearchHistory() {
        historyList.clear()
        _searchScreenState.value = SearchState.SearchHistory(historyList)
        searchInteractor.clearSearchHistory()
    }

    fun searchFocusChanged(hasFocus: Boolean, text: String) {
        if (hasFocus && text.isEmpty()) {
            _searchScreenState.value = SearchState.SearchHistory(historyList)
        }
    }

    fun clearSearchLine() {
        _searchScreenState.value = SearchState.SearchHistory(historyList)
    }

    fun addTrackToSearchHistory(track: Track) {
        if (historyList.contains(track)) {
            historyList.remove(track)
        }
        historyList.add(0, track)
        if (historyList.size > Constants.MAX_HISTORY_SIZE) {
            historyList.removeLast()
        }
        searchInteractor.saveHistory(historyList)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
*/
}