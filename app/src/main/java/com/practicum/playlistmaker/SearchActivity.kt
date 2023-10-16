package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), IClickView {

    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var query: String? = null
    private var tracks = ArrayList<Track>()
    private var tracksHistory = ArrayList<Track>()
    private lateinit var searchHistory: SearchHistory
    private val handler = Handler(Looper.getMainLooper())
    private val handlerEx = Handler(Looper.getMainLooper())
    private lateinit var inputEditText: TextView
    private lateinit var trackRecyclerViewSearchHistory: RecyclerView
    private lateinit var looper: LinearLayout
    private lateinit var notFound: LinearLayout
    private lateinit var errorConnection: LinearLayout
    private lateinit var adapterSearch: TrackAdapter
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var historyLayout: View
    private val searchRunnable = Runnable { search() }

    private var isClickAllowed = true

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val searchApiService = retrofit.create(ISearchApiService::class.java)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // in first step set all conditions
        val backButton = findViewById<FrameLayout>(R.id.back_button)
        inputEditText = findViewById<EditText>(R.id.edit_view_search)
        val clearButton = findViewById<ImageButton>(R.id.clear_text)
        trackRecyclerView = findViewById(R.id.track_recycler_view)
        errorConnection = findViewById(R.id.no_connection_error_layout)
        notFound = findViewById(R.id.not_found_layout)
        val searchRefreshButton = findViewById<Button>(R.id.search_refresh_button)
        val cleanHistoryButton = findViewById<Button>(R.id.clean_history_button)
        historyLayout = findViewById(R.id.history_layout)
        trackRecyclerViewSearchHistory = findViewById(R.id.search_history)
        looper = findViewById(R.id.progress_bar)
        val searchRunnable = Runnable { search() }
        adapterSearch = TrackAdapter(tracks, this)
        var adapterHistory = TrackAdapter(tracksHistory, this)
        trackRecyclerView.adapter = adapterSearch
        trackRecyclerViewSearchHistory.adapter = adapterHistory
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

        trackRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackRecyclerViewSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideSoftKeyboard(it)
            notFound.visibility = View.GONE
            errorConnection.visibility = View.GONE
            tracks.clear()
            adapterSearch.notifyDataSetChanged()
            refreshHistory()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            trackRecyclerView.visibility = View.VISIBLE
            notFound.visibility = View.GONE
            errorConnection.visibility = View.GONE
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus
                && inputEditText.text.isEmpty()
                && tracksHistory.isNotEmpty()
            ) {
                refreshHistory()
            } else {
                historyLayout.visibility = View.GONE
            }
        }

        cleanHistoryButton.setOnClickListener {
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
                clearButton.visibility = clearButtonVisibility(s)
                query = s.toString()
                historyLayout.visibility =
                    if (inputEditText.hasFocus()
                        && s?.isEmpty() == true
                        && tracksHistory.isNotEmpty()
                    ) View.VISIBLE else View.GONE
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                query = inputEditText.text.toString()
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        if (savedInstanceState != null) {
            inputEditText.setText(savedInstanceState.getString(SEARCH_TEXT, ""))
        }

        searchRefreshButton.setOnClickListener {
            search()
        }
    }

    private fun search() {

        if (inputEditText.text.isNotEmpty()) {

            historyLayout.visibility = View.GONE
            notFound.visibility = View.GONE
            looper.visibility = View.VISIBLE

            searchApiService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                errorConnection.visibility = View.GONE
                                looper.visibility = View.GONE
                                trackRecyclerView.visibility = View.VISIBLE
                            } else {
                                notFound.visibility = View.VISIBLE
                            }
                        } else {
                            errorConnection.visibility = View.VISIBLE
                        }
                        adapterSearch.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        tracks.clear()
                        looper.visibility = View.GONE
                        errorConnection.visibility = View.VISIBLE
                    }
                })
        } else notFound.visibility = View.VISIBLE
    }

    private fun clearButtonVisibility(s: CharSequence?): Int =
        if (s.isNullOrEmpty()) View.GONE else View.VISIBLE

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun hideSoftKeyboard(view: View) {
        val imm =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        query = savedInstanceState.getString(SEARCH_TEXT, "")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, query)
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
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}