package com.practicum.playlistmaker.ui.search.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchViewModelFactory(private val context: Context) : ViewModelProvider.AndroidViewModelFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel() as T
    }
}