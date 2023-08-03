package com.practicum.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // in first step set all conditions
        val backButton = findViewById<FrameLayout>(R.id.back_button)
        val inputEditText = findViewById<EditText>(R.id.edit_view_search)
        val clearButton = findViewById<ImageButton>(R.id.clear_text)

        clearButton.visibility = View.GONE

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
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
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
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