package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator

class SearchViewModelFactory: ViewModelProvider.Factory {

    private val searchInteractor = Creator.provideSearchInteractor()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            searchInteractor = searchInteractor
        ) as T
    }
}