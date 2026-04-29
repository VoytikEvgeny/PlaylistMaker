package com.example.playlistmaker

import android.annotation.SuppressLint
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
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var inputSearchText: String = DEFAULT_STR
    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackApiService = retrofit.create(SearchTrackApi::class.java)
    private var tracks = ArrayList<Track>()
    private var trackAdapter: TrackAdapter? = null
    private var searchTrack: String = ""
    private var searchHistory: SearchHistory? = null
    private lateinit var rvTrack: RecyclerView
    private var recyclerView: RecyclerView? = null
    private var noContentView: LinearLayout? = null
    private var noConnectView: LinearLayout? = null
    private var clearHistoryButton: Button? = null
    private var youSearchTitle: TextView? = null
    private var progressBar: ProgressBar? = null
    private var apiCallback = initApiCallback()
    private val searchRunnable = Runnable {
        doSearch()
    }
    private val handler = Handler(Looper.getMainLooper())
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.back_button)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageButton>(R.id.clearIcon)
        val apiCallback = initApiCallback()

        rvTrack = findViewById(R.id.rvTrack)
        recyclerView = initSongsRecyclerView()
        noContentView = findViewById<LinearLayout>(R.id.no_content)
        noConnectView = findViewById<LinearLayout>(R.id.no_connect)
        clearHistoryButton = findViewById<Button>(R.id.clear_history)
        youSearchTitle = findViewById<TextView>(R.id.search_history_text)

        progressBar = findViewById(R.id.progress_bar)

        clearHistoryButton?.setOnClickListener {
            tracks.clear()
            searchHistory?.clearHistory()
            allGone()
        }

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            searchEditText.clearFocus()
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                searchEditText.windowToken,
                0
            )
            tracks.clear()
            allGone()
            if (searchHistory?.tracks?.isNotEmpty() == true) {
                tracks.addAll(searchHistory!!.tracks)
                settingVisibilitySearchHistory(true)
            }
            trackAdapter!!.notifyDataSetChanged()
        }

        searchEditText.setOnFocusChangeListener { _, onFocus ->
            allGone()
            if (onFocus && searchHistory?.tracks?.isNotEmpty() == true) {
                tracks.clear()
                tracks.addAll(searchHistory!!.tracks)
                trackAdapter?.notifyDataSetChanged()
                settingVisibilitySearchHistory(true)
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    searchDebounce()
                    clearButton.visibility = View.VISIBLE
                    allGone()
                } else {
                    clearButton.visibility = View.GONE
                    allGone()
                    settingVisibilitySearchHistory(true)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                inputSearchText = s.toString()
            }
        })

        searchEditText.setOnEditorActionListener { fieldSearch, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack = fieldSearch.text.toString().trim()
                trackApiService.search(searchTrack).enqueue(apiCallback)
                handler.removeCallbacks(searchRunnable)
                doSearch()

            }
            false
        }

        val buttonReload = findViewById<Button>(R.id.btn_reload)
        buttonReload.setOnClickListener {
            trackApiService.search(searchTrack).enqueue(apiCallback)
        }

    }

    private fun allGone() {
        noConnectView?.visibility = View.GONE
        noContentView?.visibility = View.GONE
        settingVisibilitySearchHistory(false)
    }

    private fun settingVisibilitySearchHistory(visibility: Boolean) {
        if (visibility) {
            recyclerView?.visibility = View.VISIBLE
            clearHistoryButton?.visibility = View.VISIBLE
            youSearchTitle?.visibility = View.VISIBLE
        } else {
            recyclerView?.visibility = View.GONE
            clearHistoryButton?.visibility = View.GONE
            youSearchTitle?.visibility = View.GONE
        }
    }

    private fun initApiCallback(): Callback<TracksResponse> {
        return (object : Callback<TracksResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                progressBar?.visibility = View.GONE
                if (response.isSuccessful) {
                    tracks.clear()
                    val responseFromApi = response.body()?.results
                    if (responseFromApi?.isNotEmpty() == true) {
                        allGone()
                        recyclerView?.visibility = View.VISIBLE

                        tracks.addAll(responseFromApi)
                        trackAdapter!!.notifyDataSetChanged()
                    } else {
                        allGone()
                        noContentView?.visibility = View.VISIBLE
                    }
                } else {
                    allGone()
                    noContentView?.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                progressBar?.visibility = View.GONE
                allGone()
                noConnectView?.visibility = View.VISIBLE
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initSongsRecyclerView(): RecyclerView {
        val sharedPreferences = getSharedPreferences(APPLICATION_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)
        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (TRACK_HISTORY == key) {
                tracks.clear()
                tracks.addAll(searchHistory!!.tracks)
                allGone()
                rvTrack.visibility = View.VISIBLE
                youSearchTitle?.visibility = View.VISIBLE
                clearHistoryButton?.visibility = View.VISIBLE
                trackAdapter!!.notifyDataSetChanged()
            }
        }
        trackAdapter = TrackAdapter(tracks, searchHistory!!)
        rvTrack.adapter = trackAdapter
        return rvTrack
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, inputSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(SAVED_TEXT, DEFAULT_STR)
        findViewById<EditText>(R.id.searchEditText).setText(inputSearchText)
    }

    private fun doSearch() {
        allGone()
        progressBar?.visibility = View.VISIBLE
        trackApiService.search(inputSearchText).enqueue(apiCallback)
    }

    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val DEFAULT_STR = ""
        const val imdbBaseUrl = "https://itunes.apple.com"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
