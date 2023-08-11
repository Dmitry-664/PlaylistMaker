package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }
    private lateinit var inputEditText: EditText
    private var currentSearchQuery: String = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, currentSearchQuery)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        if (savedInstanceState != null) {
            currentSearchQuery = savedInstanceState.getString(SEARCH_QUERY, "")
            inputEditText.setText(currentSearchQuery)
        }

        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener{
            finish()
        }

        inputEditText = findViewById<EditText>(R.id.inputEditText)
        inputEditText.setOnClickListener{
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.visibility = View.INVISIBLE
        clearButton.setOnClickListener{
            inputEditText.text.clear()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            inputEditText.clearFocus()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)=Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                currentSearchQuery = inputEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable?)=Unit
        }
        inputEditText.addTextChangedListener(textWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }
}