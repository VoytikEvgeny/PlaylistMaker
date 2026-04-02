package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var inputSearchText: String = DEFAULT_STR


    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackApiService = retrofit.create(SearchTrackApi::class.java)
    private var tracks = ArrayList<Track>()
    private var trackAdapter: TrackAdapter = TrackAdapter(tracks)
    private var searchTrack: String = ""

    private lateinit var rvTrack: RecyclerView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.back_button)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()

            finish()
        }

        rvTrack = findViewById(R.id.rvTrack)

        val recyclerView = initSongsRecyclerView()
        val noContentView = findViewById<LinearLayout>(R.id.no_content)
        val noConnectView = findViewById<LinearLayout>(R.id.no_connect)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageButton>(R.id.clearIcon)


        val apiCallback = initApiCallback(recyclerView, noConnectView, noContentView)

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            searchEditText.clearFocus()
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                searchEditText.windowToken,
                0
            )
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            allGone(recyclerView, noConnectView, noContentView)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    clearButton.visibility = View.VISIBLE
                } else {
                    clearButton.visibility = View.GONE
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
            }
            false
        }

        val buttonReload = findViewById<Button>(R.id.btn_reload)
        buttonReload.setOnClickListener {
            trackApiService.search(searchTrack).enqueue(apiCallback)
        }

    }

    private fun allGone(
        recyclerView: RecyclerView,
        noConnectView: LinearLayout,
        noContentView: LinearLayout
    ) {
        recyclerView.visibility = View.GONE
        noConnectView.visibility = View.GONE
        noContentView.visibility = View.GONE
    }

    private fun initApiCallback(
        recyclerView: RecyclerView,
        noConnectView: LinearLayout,
        noContentView: LinearLayout
    ): Callback<TracksResponse> {
        return (object : Callback<TracksResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                if (response.isSuccessful) {
                    tracks.clear()
                    val responseFromApi = response.body()?.results
                    if (responseFromApi?.isNotEmpty() == true) {
                        allGone(recyclerView, noConnectView, noContentView)
                        recyclerView.visibility = View.VISIBLE

                        tracks.addAll(responseFromApi)
                        trackAdapter.notifyDataSetChanged()
                    } else {
                        allGone(recyclerView, noConnectView, noContentView)
                        noContentView.visibility = View.VISIBLE
                    }
                } else {
                    allGone(recyclerView, noConnectView, noContentView)
                    noContentView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                allGone(recyclerView, noConnectView, noContentView)
                noConnectView.visibility = View.VISIBLE
            }
        })
    }

    private fun initSongsRecyclerView(): RecyclerView {
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

    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val DEFAULT_STR = ""

        const val imdbBaseUrl = "https://itunes.apple.com"

    }
}
