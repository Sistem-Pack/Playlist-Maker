package com.practicum.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.data.search.IClickView
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Consts
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.network.IDataLoadCallback
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.search.models.SearchHistory
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.track.TrackAdapter
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModelFactory

class SearchActivity : AppCompatActivity(), IClickView, IDataLoadCallback {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel

    private var query: String? = null

    private lateinit var searchHistory: SearchHistory
    private val handler = Handler(Looper.getMainLooper())
    //private lateinit var inputEditText: TextView
    //private lateinit var trackRecyclerViewSearchHistory: RecyclerView
    private lateinit var looper: LinearLayout
    private lateinit var notFound: LinearLayout
    private lateinit var errorConnection: LinearLayout
    private lateinit var adapterSearch: TrackAdapter
    private lateinit var adapterHistory: TrackAdapter
    //private lateinit var trackRecyclerView: RecyclerView
    private lateinit var historyLayout: View
    private val searchRunnable = Runnable { search() }

    private var isClickAllowed = true

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchViewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(this)
        )[SearchViewModel::class.java]

        binding.backButton.setOnClickListener {
            finish()
        }

        adapterSearch = TrackAdapter(tracks, this)
        adapterHistory = TrackAdapter(tracksHistory, this)


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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        query = savedInstanceState.getString(Consts.SEARCH_TEXT, "")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Consts.SEARCH_TEXT, query)
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
    }

}