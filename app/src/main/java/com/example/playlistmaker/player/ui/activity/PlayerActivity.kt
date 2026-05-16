package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.ui.TrackScreenState
import com.example.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {
    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityPlayerBinding
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {

        binding.commonButton.setOnClickListener {
            if (viewModel.getPlayStatusLiveData().value?.isPlaying == true) {
                viewModel.pause()
            } else {
                viewModel.play()
            }
        }
        binding.backButton.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupObservers() {
        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is TrackScreenState.Content -> updateUI(screenState.trackModel)
                is TrackScreenState.Loading -> changeContentVisibility(loading = true)
            }
        }
        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            if (playStatus.isPlaying != isPlaying) updatePlayButton(playStatus.isPlaying)
            binding.progress.text = playStatus.progress
        }
    }

    private fun updateUI(track: Track) {
        changeContentVisibility(loading = false)
        Glide.with(this)
            .load(track.getHighResArtworkUrl())
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8f, this)))
            .into(binding.cover)
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.durabilityVal.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
        binding.albumVal.text = track.collectionName
        binding.yearVal.text =
            SimpleDateFormat("yyyy", Locale.getDefault()).format(track.trackTimeMillis.toLong())
        binding.genreVal.text = track.primaryGenreName
        binding.countryVal.text = track.country
    }

    private fun changeContentVisibility(loading: Boolean) {
    }

    private fun updatePlayButton(status: Boolean) {
        if (status) {
            binding.commonButton.setImageResource(R.drawable.ic_pause)
        } else {
            binding.commonButton.setImageResource(R.drawable.ic_play)
        }
        isPlaying = status
    }
}