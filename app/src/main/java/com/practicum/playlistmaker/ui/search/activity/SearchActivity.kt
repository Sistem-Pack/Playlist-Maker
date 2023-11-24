package com.practicum.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Consts
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModelFactory
import com.practicum.playlistmaker.ui.search.view_model.SearchState

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel

    private var searchText: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchViewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(this)
        )[SearchViewModel::class.java]

        searchViewModel.setTrackAdapters(binding.trackRecyclerView, binding.searchHistory)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.clearText.setOnClickListener {
            binding.editViewSearch.setText("")
            (getSystemService(Context.INPUT_METHOD_SERVICE) as? android.view.inputmethod.InputMethodManager)?.hideSoftInputFromWindow(
                binding.editViewSearch.windowToken,
                0
            )
            searchViewModel.showHistoryTracks()
        }

        binding.searchRefreshButton.setOnClickListener {
            searchViewModel.searchDebounce(binding.editViewSearch.text.toString())
        }

        binding.cleanHistoryButton.setOnClickListener {
            searchViewModel.clearSearchHistory()
        }

        binding.editViewSearch.setOnFocusChangeListener { _, hasFocus ->
            searchViewModel.searchFocusChanged(hasFocus, binding.editViewSearch.text.toString())
        }

        binding.editViewSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.editViewSearch.hasFocus() && searchText.isEmpty()) {
                    searchViewModel.showHistoryTracks()
                } else if (searchText != binding.editViewSearch.text.toString()) {
                    searchViewModel.searchDebounce(searchText)
                }
            }
            false
        }

        searchViewModel.searchScreenState.observe(this) { state ->
            render(state)
        }

        // отправляем все на поиск
        binding.editViewSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearText.visibility = clearButtonVisibility(s)
                searchText = s?.toString() ?: ""
                if (binding.editViewSearch.hasFocus() && searchText.isEmpty()) {
                    searchViewModel.showHistoryTracks()
                } else searchViewModel.searchDebounce(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = binding.editViewSearch.text.toString()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun render(state: SearchState) {
        if ((binding.editViewSearch.text.toString() == "") && state is SearchState.Content)
            searchViewModel.showHistoryTracks()
        if (state is SearchState.Content && binding.trackRecyclerView.adapter != null) {
            binding.trackRecyclerView.adapter!!.notifyDataSetChanged()
        } else if (state is SearchState.SearchHistory && binding.searchHistory.adapter != null) {
            binding.searchHistory.adapter!!.notifyDataSetChanged()
        }
        binding.trackRecyclerView.visibility =
            if (state is SearchState.Content) View.VISIBLE else View.GONE
        binding.noConnectionErrorLayout.visibility =
            if (state is SearchState.Error) View.VISIBLE else View.GONE
        binding.notFoundLayout.visibility =
            if (state is SearchState.Empty) View.VISIBLE else View.GONE
        binding.historyLayout.visibility =
            if (state is SearchState.SearchHistory) View.VISIBLE else View.GONE
        binding.progressBar.visibility =
            if (state is SearchState.Loading) View.VISIBLE else View.GONE
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(Consts.SEARCH_TEXT, "")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Consts.SEARCH_TEXT, searchText)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

}