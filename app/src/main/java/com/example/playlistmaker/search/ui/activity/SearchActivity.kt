package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.APPLICATION_PREFERENCES
import com.example.playlistmaker.SEARCH_DEBOUNCE_DELAY
import com.example.playlistmaker.TRACK_HISTORY
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.search.ui.SearchHistoryService
import com.example.playlistmaker.search.domain.models.SearchState

class SearchActivity : AppCompatActivity() {
    private var inputSearchText: String =
        DEFAULT_STR
    private lateinit var binding: ActivitySearchBinding
    private var searchTrack: String = ""
    private val tracks = ArrayList<Track>()
    private var trackAdapter: TrackAdapter? = null
    private var searchHistoryService: SearchHistoryService? = null
    private var recyclerView: RecyclerView? = null
    private var noContentView: LinearLayout? = null
    private var noConnectView: LinearLayout? = null
    private var clearHistoryButton: Button? = null
    private var youSearchTitle: TextView? = null
    private var progressBar: ProgressBar? = null
    private val viewModel by viewModel<SearchViewModel>()

    @Volatile
    private var searchIsScheduled = false

    @Volatile
    private var searchInProgress = false
    private var searchRunnable: Runnable = initSearchRunnable(emptyList())
    private val handler = Handler(Looper.getMainLooper())

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = initSongsRecyclerView()
        noContentView = binding.noContent
        noConnectView = binding.noConnect
        clearHistoryButton = binding.clearHistory
        youSearchTitle = binding.searchHistoryText
        progressBar = binding.progressBar

        val searchEditText = binding.searchEditText
        val clearButton = binding.clearIcon

        this.clearHistoryButton?.setOnClickListener {
            tracks.clear()
            searchHistoryService?.clearHistory()
            allGone()
        }

        clearButton.setOnClickListener {
            showHistoryIfItsNeeded(searchEditText)
        }

        searchEditText.setOnFocusChangeListener { _, onFocus ->
            allGone()
            if (onFocus && searchHistoryService?.tracks?.isNotEmpty() == true) {
                tracks.clear()
                tracks.addAll(searchHistoryService!!.tracks)
                trackAdapter?.notifyDataSetChanged()
                recyclerView?.visibility = View.VISIBLE
                clearHistoryButton?.visibility = View.VISIBLE
                youSearchTitle?.visibility = View.VISIBLE

            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchTrack = s.toString().trim()
                if (!s.isNullOrEmpty()) {
                    handler.removeCallbacks(searchRunnable)
                    if (!searchIsScheduled) {
                        searchIsScheduled = true
                        handler.postDelayed(
                            { doSearch() },
                            SEARCH_DEBOUNCE_DELAY
                        )
                    }
                    clearButton.visibility = View.VISIBLE
                } else {
                    clearButton.visibility = View.GONE
                    showHistoryIfItsNeeded(searchEditText)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                inputSearchText = s.toString()
            }

        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handler.removeCallbacks(searchRunnable)
                doSearch()
            }
            false
        }
        binding.btnReload.setOnClickListener {
            doSearch()
        }

        viewModel.getState().observe(this) { state ->
            render(state)
        }
        binding.backButton.setNavigationOnClickListener {
            finish()
        }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Error -> showError(state.message)
            is SearchState.Content -> showFound(state.data)
        }
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun showFound(foundTracks: List<Track>) {

        val currentRunnable = searchRunnable
        handler.removeCallbacks(currentRunnable)
        val newSearchRunnable: Runnable = initSearchRunnable(foundTracks)
        searchRunnable = newSearchRunnable
        handler.post(newSearchRunnable)
        searchIsScheduled = false
        searchInProgress = false

    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        onBackPressedDispatcher.onBackPressed()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun showHistoryIfItsNeeded(searchEditText: EditText) {
        searchEditText.text.clear()
        searchEditText.clearFocus()
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(searchEditText.windowToken, 0)
        tracks.clear()
        if (searchHistoryService?.tracks?.isNotEmpty() == true) {
            tracks.addAll(searchHistoryService!!.tracks)
            allGone()
            youSearchTitle?.visibility = View.VISIBLE
            recyclerView?.visibility = View.VISIBLE
            clearHistoryButton?.visibility = View.VISIBLE
        }
        trackAdapter!!.notifyDataSetChanged()
    }

    private fun allGone() {
        recyclerView?.visibility = View.GONE
        noContentView?.visibility = View.GONE
        noConnectView?.visibility = View.GONE
        clearHistoryButton?.visibility = View.GONE
        youSearchTitle?.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initSongsRecyclerView(): RecyclerView {
        val trackRecyclerView = binding.rvTrack

        val sharedPreferences = getSharedPreferences(APPLICATION_PREFERENCES, MODE_PRIVATE)
        searchHistoryService =
            SearchHistoryService(
                sharedPreferences,
                viewModel.gson()
            )

        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (TRACK_HISTORY == key) {
                tracks.clear()
                tracks.addAll(searchHistoryService!!.tracks)
                allGone()
                trackRecyclerView.visibility = View.VISIBLE
                youSearchTitle?.visibility = View.VISIBLE
                clearHistoryButton?.visibility = View.VISIBLE
                trackAdapter!!.notifyDataSetChanged()
            }
        }
        trackAdapter = TrackAdapter(
            tracks,
            searchHistoryService!!,
            viewModel.gson()
        )
        trackRecyclerView.adapter = trackAdapter
        return trackRecyclerView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, inputSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(
            SAVED_TEXT,
            DEFAULT_STR
        )
        binding.searchEditText.setText(inputSearchText)
    }

    private fun doSearch() {
        if (searchTrack.isEmpty()) {
            searchIsScheduled = false
            return
        }
        if (searchInProgress) return
        searchInProgress = true
        allGone()
        viewModel.loadData(searchTrack)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initSearchRunnable(foundTracks: List<Track>): Runnable {
        return Runnable {
            progressBar?.visibility = View.GONE
            if (foundTracks.isNotEmpty()) {
                tracks.clear()
                allGone()
                recyclerView?.visibility = View.VISIBLE
                tracks.addAll(foundTracks)
                trackAdapter!!.notifyDataSetChanged()
            } else {
                allGone()
                noContentView?.visibility = View.VISIBLE

            }
        }
    }

    companion object {
        private const val SAVED_TEXT = "SAVED_TEXT"
        private const val DEFAULT_STR = ""
    }
}