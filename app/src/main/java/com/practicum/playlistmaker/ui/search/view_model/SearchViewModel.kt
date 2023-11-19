package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.search.models.Track

class SearchViewModel : ViewModel() {

    //private var tracks = ArrayList<Track>()
    //private var tracksHistory = ArrayList<Track>()

    private val _tracksState = MutableLiveData<ArrayList<Track>>()
    private val _tracksHistoryState = MutableLiveData<ArrayList<Track>>()
    val tracksState: LiveData<ArrayList<Track>> = _tracksState
    val tracksHistoryState: LiveData<ArrayList<Track>> = _tracksHistoryState

    init {
        //_tracksState.value = settingsInteractor.getThemeSettings()
    }




}