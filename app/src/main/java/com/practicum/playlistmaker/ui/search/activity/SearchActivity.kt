package com.practicum.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Consts
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModelFactory
import com.practicum.playlistmaker.ui.search.view_model.SearchState


/*
// in first step set all conditions
        //inputEditText = findViewById<EditText>(R.id.edit_view_search)
        //val clearButton = findViewById<ImageButton>(R.id.clear_text)
        //trackRecyclerView = findViewById(R.id.track_recycler_view)
        errorConnection = findViewById(R.id.no_connection_error_layout)
        notFound = findViewById(R.id.not_found_layout)
        val searchRefreshButton = findViewById<Button>(R.id.search_refresh_button)
        val cleanHistoryButton = findViewById<Button>(R.id.clean_history_button)
        historyLayout = findViewById(R.id.history_layout)
        //trackRecyclerViewSearchHistory = findViewById(R.id.search_history)
        looper = findViewById(R.id.progress_bar)
        val searchRunnable = Runnable { search() }

        //trackRecyclerView.adapter = adapterSearch
        //trackRecyclerViewSearchHistory.adapter = adapterHistory
        searchHistory = SearchHistory(applicationContext as App)


        private var query: String? = null

    private lateinit var searchHistory: SearchHistory
    private val handler = Handler(Looper.getMainLooper())
    //private lateinit var inputEditText: TextView
    //private lateinit var trackRecyclerViewSearchHistory: RecyclerView
    private lateinit var looper: LinearLayout
    private lateinit var notFound: LinearLayout
    private lateinit var errorConnection: LinearLayout

    //private lateinit var trackRecyclerView: RecyclerView
    private lateinit var historyLayout: View
    private val searchRunnable = Runnable { search() }

    private var isClickAllowed = true


*/
class SearchActivity : AppCompatActivity()/*, IClickView, IDataLoadCallback*/ {

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

        searchViewModel.searchScreenState.observe(this) { state ->
            render(state)
        }

        binding.editViewSearch.setOnFocusChangeListener { _, hasFocus ->
            searchViewModel.searchFocusChanged(hasFocus, binding.editViewSearch.text.toString())
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

    private fun renderEx(visibleType: String) {
        binding.trackRecyclerView.visibility = if (visibleType == trackRecyclerView) { View.GONE } else View.VISIBLE
        binding.noConnectionErrorLayout.visibility = View.GONE
        binding.notFoundLayout.visibility = View.GONE
        binding.historyLayout.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> {
                binding.trackRecyclerView.visibility = View.GONE
                binding.noConnectionErrorLayout.visibility = View.GONE
                binding.notFoundLayout.visibility = View.GONE
                binding.historyLayout.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }

            is SearchState.Content -> {
                binding.progressBar.visibility = View.GONE
                binding.noConnectionErrorLayout.visibility = View.GONE
                binding.notFoundLayout.visibility = View.GONE
                binding.historyLayout.visibility = View.GONE
                binding.trackRecyclerView.visibility = View.VISIBLE
                //trackAdapter.setTracks(state.tracks)
            }

            is SearchState.SearchHistory -> {
                binding.trackRecyclerView.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.noConnectionErrorLayout.visibility = View.GONE
                binding.notFoundLayout.visibility = View.GONE
                binding.historyLayout.visibility = View.VISIBLE
                /*trackHistoryAdapter.setTracks(state.tracks)
                if(state.tracks.isNotEmpty()) {
                    setStatus(SearchStatus.HISTORY)
                } else {
                    setStatus(SearchStatus.ALL_GONE)
                }*/
            }

            is SearchState.Empty -> {
                binding.trackRecyclerView.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.noConnectionErrorLayout.visibility = View.GONE
                binding.historyLayout.visibility = View.GONE
                binding.notFoundLayout.visibility = View.VISIBLE
            }

            is SearchState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.trackRecyclerView.visibility = View.GONE
                binding.notFoundLayout.visibility = View.GONE
                binding.historyLayout.visibility = View.GONE
                binding.noConnectionErrorLayout.visibility = View.VISIBLE
            }

        }
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
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}

/*binding.editViewSearch.setOnEditorActionListener { _, actionId, _ ->

  /*binding.trackRecyclerView.visibility = View.VISIBLE
    notFound.visibility = View.GONE
    errorConnection.visibility = View.GONE
    if (actionId == EditorInfo.IME_ACTION_DONE) {
        search()
    }
    false*/
}*/


/*
fun refreshHistory() {
    tracksHistory.clear()
    tracksHistory.addAll(searchHistory.getTracksHistory())
    historyLayout.visibility = if (tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE
    adapterHistory.notifyDataSetChanged()
}

if (searchHistory.getTracksHistory().isNotEmpty()) {
    refreshHistory()
}

binding.editViewSearch.setOnEditorActionListener { _, actionId, _ ->
    binding.trackRecyclerView.visibility = View.VISIBLE
    notFound.visibility = View.GONE
    errorConnection.visibility = View.GONE
    if (actionId == EditorInfo.IME_ACTION_DONE) {
        search()
    }
    false
}

binding.editViewSearch.setOnFocusChangeListener { _, hasFocus ->
    if (hasFocus
        && binding.editViewSearch.text.isEmpty()
        && tracksHistory.isNotEmpty()
    ) {
        refreshHistory()
    } else {
        historyLayout.visibility = View.GONE
    }
}

binding.cleanHistoryButton.setOnClickListener {
    historyLayout.visibility = View.GONE
    tracksHistory.clear()
    searchHistory.clean()
}

// at now all work process
val simpleTextWatcher = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // empty
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        binding.clearText.visibility = clearButtonVisibility(s)
        query = s.toString()
        historyLayout.visibility =
            if (binding.editViewSearch.hasFocus()
                && s?.isEmpty() == true
                && tracksHistory.isNotEmpty()
            ) View.VISIBLE else View.GONE
        searchDebounce()
    }

    override fun afterTextChanged(s: Editable?) {
        query = binding.editViewSearch.text.toString()
    }
}
binding.editViewSearch.addTextChangedListener(simpleTextWatcher)

if (savedInstanceState != null) {
    binding.editViewSearch.setText(savedInstanceState.getString(Consts.SEARCH_TEXT, ""))
}

searchRefreshButton.setOnClickListener {
    search()
}
}
private fun search() {
if (binding.editViewSearch.text.isNotEmpty()) {
    errorConnection.visibility = View.GONE
    historyLayout.visibility = View.GONE
    notFound.visibility = View.GONE
    looper.visibility = View.VISIBLE
    Creator.tracksInteractor().search(binding.editViewSearch.text.toString(), this)
} else notFound.visibility = View.VISIBLE
}

private fun clearButtonVisibility(s: CharSequence?): Int =
if (s.isNullOrEmpty()) View.GONE else View.VISIBLE

private fun searchDebounce() {
handler.removeCallbacks(searchRunnable)
handler.postDelayed(searchRunnable, Consts.SEARCH_DEBOUNCE_DELAY)
}

override fun onClick(track: Track) {
if (clickDebounce()) {
    searchHistory.addTrack(track)
    Intent(this, PlayerActivity()::class.java).apply {
        putExtra("track", track)
        startActivity(this)
    }
}
}

private fun clickDebounce(): Boolean {
val current = isClickAllowed
if (isClickAllowed) {
    isClickAllowed = false
    handler.postDelayed({ isClickAllowed = true }, Consts.CLICK_DEBOUNCE_DELAY)
}
return current
}

override fun onDataLoaded(tracksL: List<Track>) {
runOnUiThread {
    tracks.clear()
    looper.visibility = View.GONE
    if (tracksL.isNotEmpty()) {
        tracks.addAll(tracksL)
        errorConnection.visibility = View.GONE
        binding.trackRecyclerView.visibility = View.VISIBLE
        adapterSearch.notifyDataSetChanged()
    } else {
        notFound.visibility = View.VISIBLE
    }

}
}

override fun onError(code: Int) {
if (code != 200) {
    runOnUiThread {
        tracks.clear()
        looper.visibility = View.GONE
        errorConnection.visibility = View.VISIBLE
    }
}
}*/