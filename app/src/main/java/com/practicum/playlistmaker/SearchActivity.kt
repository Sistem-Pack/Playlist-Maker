package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SearchActivity : AppCompatActivity() {

    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    private var query: String? = null
    private var tracks = ArrayList<Track>()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val searchApiService = retrofit.create(SearchApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // in first step set all conditions
        val backButton = findViewById<FrameLayout>(R.id.back_button)
        val inputEditText = findViewById<EditText>(R.id.edit_view_search)
        val clearButton = findViewById<ImageButton>(R.id.clear_text)
        val trackRecyclerView = findViewById<RecyclerView>(R.id.track_recycler_view)
        val errorConnection = findViewById<LinearLayout>(R.id.no_connection_error_layout)
        val notFound = findViewById<LinearLayout>(R.id.not_found_layout)
        val searchRefreshButton = findViewById<Button>(R.id.search_refresh_button)

        trackRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        fun search() {

            searchApiService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            errorConnection.visibility = View.GONE
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                            } else {
                                notFound.visibility = View.VISIBLE
                            }
                        } else {
                            errorConnection.visibility = View.VISIBLE
                        }
                        trackRecyclerView.adapter = TrackAdapter(tracks)
                        TrackAdapter(tracks).notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        tracks.clear()
                        errorConnection.visibility = View.VISIBLE
                    }
                })
        }

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideSoftKeyboard(it)
            tracks.clear()
            trackRecyclerView.adapter = TrackAdapter(tracks)
            TrackAdapter(tracks).notifyDataSetChanged()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            trackRecyclerView.visibility = View.VISIBLE
            notFound.visibility = View.GONE
            errorConnection.visibility = View.GONE
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        // at now all work process
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                query = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
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

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
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

}