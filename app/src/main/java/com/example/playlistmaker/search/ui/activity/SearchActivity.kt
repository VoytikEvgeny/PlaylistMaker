package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.playlistmaker.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchHistoryAdapter
import com.example.playlistmaker.search.ui.TrackListAdapter
import com.example.playlistmaker.search.ui.TrackState
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private var searchString: String = ""
    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var simpleTextWatcher: TextWatcher
    private val trackListAdapter = TrackListAdapter()
    private val searchHistoryAdapter = SearchHistoryAdapter()
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.rvTrack.adapter = trackListAdapter
        binding.historyRecyclerView.adapter = searchHistoryAdapter
        setupObserves()
        setupListeners()
        setupContent()
    }

    private fun setupObserves() {
        viewModel.getSearchLiveData().observe(this) {
            render(it)
        }
        viewModel.getHistoryLiveData().observe(this) {
            updateHistoryUi(it)
        }
    }

    private fun setupListeners() {
        trackListAdapter.onItemClick = { track -> openAudioPlayer(track) }
        searchHistoryAdapter.onItemClick = { track -> openAudioPlayer(track) }
        simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                binding.searchHistoryGroup.isVisible =
                    if (binding.searchEditText.hasFocus() && s?.isEmpty() == true && searchHistoryAdapter.searchHistoryTrackList.isNotEmpty()) true else false
                binding.searchHistoryText.isVisible = binding.searchHistoryGroup.isVisible
                viewModel.searchDebounce(
                    s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
                searchString = binding.searchEditText.text.toString()

            }
        }
        simpleTextWatcher.let { binding.searchEditText.addTextChangedListener(it) }
        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            binding.searchHistoryGroup.isVisible =
                if (hasFocus && binding.searchEditText.text.isEmpty() && searchHistoryAdapter.searchHistoryTrackList.isNotEmpty()) true else false
            binding.searchHistoryText.isVisible = binding.searchHistoryGroup.isVisible
        }
        binding.backButton.setNavigationOnClickListener {
            finish()
        }
        binding.clearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
            binding.searchHistoryGroup.isVisible = false
            binding.searchHistoryText.isVisible = binding.searchHistoryGroup.isVisible
        }
        binding.btnReload.setOnClickListener {
            viewModel.searchDebounce(searchString)
            closeErrorMessage()
        }
        binding.clearIcon.setOnClickListener {
            binding.searchEditText.setText("")
            clearSearchList()
            closeErrorMessage()
            hideKeyboard()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearSearchList() {
        trackListAdapter.removeItems()
        trackListAdapter.notifyDataSetChanged()
    }

    private fun setupContent() {
        viewModel.getHistoryList()
        binding.searchEditText.setText(searchString)
    }

    private fun render(state: TrackState) {
        when (state) {
            is TrackState.Content -> showSearchContent(state.tracks)
            is TrackState.Empty -> showError(binding.noContent)
            is TrackState.Error -> showError(binding.noConnect)
            is TrackState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        closeErrorMessage()
        binding.rvTrack.visibility = View.GONE
        binding.searchHistoryGroup.visibility = View.GONE
        binding.searchHistoryText.visibility = binding.searchHistoryGroup.visibility
        binding.progressBar.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchContent(tracks: List<Track>) {
        closeErrorMessage()
        binding.progressBar.visibility = View.GONE
        binding.searchHistoryGroup.visibility = View.GONE
        binding.searchHistoryText.visibility = binding.searchHistoryGroup.visibility
        binding.rvTrack.visibility = View.VISIBLE
        trackListAdapter.removeItems()
        trackListAdapter.trackList.addAll(tracks)
        trackListAdapter.notifyDataSetChanged()
    }

    private fun showError(view: LinearLayout) {
        binding.progressBar.visibility = View.GONE
        binding.searchHistoryGroup.visibility = View.GONE
        binding.searchHistoryText.visibility = binding.searchHistoryGroup.visibility
        binding.rvTrack.visibility = View.GONE
        view.isVisible = true
        hideKeyboard()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun openAudioPlayer(track: Track) {
        if (clickDebounce()) {
            viewModel.addTrackToHistory(track)
            startActivity(Intent(this, PlayerActivity::class.java))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateHistoryUi(list: MutableList<Track>) {
        searchHistoryAdapter.searchHistoryTrackList = list
        searchHistoryAdapter.notifyDataSetChanged()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    private fun closeErrorMessage() {
        binding.noContent.isVisible = false
        binding.noConnect.isVisible = false
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleTextWatcher.let { binding.searchEditText.removeTextChangedListener(it) }
    }
}