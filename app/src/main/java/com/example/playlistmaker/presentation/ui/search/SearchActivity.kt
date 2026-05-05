package com.example.playlistmaker.presentation.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.useCase.TrackRepositoryInteractor
import com.example.playlistmaker.presentation.adapters.TrackAdapter
import com.example.playlistmaker.presentation.adapters.TrackHistoryAdapter

const val DELAYED = 2000L

class SearchActivity : AppCompatActivity() {
    private var textInput: String? = null
    private lateinit var editedText: EditText
    private val trackManager = Creator.getTrackManager()
    private lateinit var trackInteractor: TrackRepositoryInteractor
    private val mapper = Creator.getMapper()
    private var listOfSongs: ArrayList<Track> = ArrayList()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var context: Context
    lateinit var historyAdapter: TrackHistoryAdapter

    lateinit var noContentView: LinearLayout

    lateinit var noConnectView: LinearLayout

    lateinit var refreshButton: Button

    //
    lateinit var searchHistoryText: TextView
    lateinit var clearHistory: Button
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var clearInput: ImageView
    lateinit var historyRecyclerView: RecyclerView
    private val adapter = TrackAdapter(this, listOfSongs)
    val searchRunnable = Runnable { search() }

    @SuppressLint("MissingInflatedId", "CutPasteId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        context = this
        trackManager.initializeHistory(context)
        trackInteractor = Creator.provideTrackUseCase(
            Creator.provideTrackRepository(
                Creator.provideNetworkClient()
            )
        )

        historyAdapter = TrackHistoryAdapter(
            context,
            trackManager.getTrackHistory().map { mapper.map(it) })//из дто в domain

        noContentView = findViewById<LinearLayout>(R.id.no_content)
        noConnectView = findViewById<LinearLayout>(R.id.no_connect)
        refreshButton = findViewById(R.id.btn_reload)
        //
        searchHistoryText = findViewById(R.id.search_history_text)
        clearHistory = findViewById(R.id.clear_history)
        progressBar = findViewById(R.id.progress_bar)

        recyclerView = findViewById<RecyclerView>(R.id.rvTrack)
        historyRecyclerView = findViewById<RecyclerView>(R.id.historyRecyclerView)

        editedText = findViewById(R.id.searchEditText)
        clearInput = findViewById<ImageView>(R.id.clearIcon)

        allViewGone()

        if (trackManager.getTrackHistory().isEmpty()) {
            historyGone()
        }

        recyclerView.adapter = adapter
        //
        historyRecyclerView.adapter = historyAdapter
        //
        clearInput.visibility = View.GONE

        editedText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearInput.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    allViewGone()
                    recyclerView.visibility = View.GONE
                    trackInteractor.destroy()
                    val currentHistory = trackManager.getTrackHistory()
                    if (currentHistory.isNotEmpty()) {
                        historyVisible()
                        historyRecyclerView.visibility = View.VISIBLE
                        historyAdapter.updateData(currentHistory.map { mapper.map(it) })
                    } else {
                        historyGone()
                        historyRecyclerView.visibility = View.GONE
                    }
                } else {
                    startSearch()
                    historyGone()
                    historyRecyclerView.visibility = View.GONE
                }

            }

        })

        clearInput.setOnClickListener {
            trackInteractor.destroy()
            editedText.setText("")
            hideKeyboard(it)
            allViewGone()
            recyclerView.visibility = View.GONE

        }

        clearHistory.setOnClickListener {
            trackManager.deleteHistory(context)
            Creator.getSharedPrefs().saveHistory(trackManager.getTrackHistory(), context)
            historyGone()
            historyRecyclerView.visibility = View.GONE
            historyAdapter.notifyDataSetChanged()
        }

        findViewById<Toolbar>(R.id.back_button).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
        if (savedInstanceState != null) {
            editedText.setText(savedInstanceState.getString("searchText"))
        }
    }

    fun startSearch() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, DELAYED)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun search() {
        val query = editedText.text.toString()
        if (query.isNullOrEmpty()) {
            listOfSongs.clear()
            allViewGone()
            recyclerView.visibility = View.GONE
            adapter.notifyDataSetChanged()
        } else {
            allViewGone()
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            trackInteractor.execute(query) { result ->
                runOnUiThread {
                    result.onSuccess { trackList ->
                        progressBar.visibility = View.GONE
                        listOfSongs.clear()
                        if (trackList.isNotEmpty()) {
                            listOfSongs.addAll(trackList)
                            recyclerView.visibility = View.VISIBLE
                            allViewGone()
                        } else {
                            listOfSongs.clear()
                            noContentMessage()
                        }
                        adapter.notifyDataSetChanged()
                    }
                    result.onFailure {
                        progressBar.visibility = View.GONE
                        refreshButton.setOnClickListener { search() }
                        noConnectMessage()
                    }
                }
            }

            adapter.notifyDataSetChanged()
        }
    }

    fun noConnectMessage() {
        recyclerView.visibility = View.GONE
        noContentView.visibility = View.GONE
        noConnectView.visibility = View.VISIBLE
        refreshButton.visibility = View.VISIBLE
    }

    fun noContentMessage() {
        recyclerView.visibility = View.GONE
        noContentView.visibility = View.VISIBLE
        noConnectView.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    fun historyVisible() {
        searchHistoryText.visibility = View.VISIBLE
        clearHistory.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    fun allViewGone() {
        noContentView.visibility = View.GONE
        noConnectView.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    fun historyGone() {
        searchHistoryText.visibility = View.GONE
        clearHistory.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchText", textInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editedText.setText(savedInstanceState.getString("searchText"))
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}