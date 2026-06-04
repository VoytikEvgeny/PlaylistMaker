package com.example.playlistmaker.player.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.CLICKED_TRACK_CONTENT
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.ui.PlayerState
import com.example.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Objects

class PlayerActivity : AppCompatActivity() {
    private lateinit var commonButton: ImageButton
    private var playerState = PlayerViewModel.STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackJson = intent.getStringExtra(CLICKED_TRACK_CONTENT)
        val track = viewModel.fromJson(trackJson, Track::class.java)
        val trackIcon = binding.cover
        val durability = binding.durabilityVal
        commonButton = binding.commonButton

        Glide.with(this)
            .load(track.getHighResArtworkUrl())
            .fitCenter()
            .placeholder(R.drawable.ic_placeholder_45)
            .transform(RoundedCorners(dpToPx(8.0f, this)))
            .into(trackIcon)
        durability.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        setText("00:00", null, binding.progress)
        setText(track.trackName, null, binding.trackName)
        setText(track.artistName, null, binding.artistName)
        setText(track.collectionName, binding.album, binding.albumVal)
        setText(track.primaryGenreName, binding.genre, binding.genreVal)
        setText(track.country, binding.country, binding.countryVal)

        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = track.releaseDate.let { inputFormat.parse(it) }
            val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            setText(
                outputFormat.format(date!!),
                binding.year,
                binding.yearVal
            )
        } catch (exc: Exception) {
            setText(
                "",
                binding.year,
                binding.yearVal
            )
        }

        viewModel.preparePlayer(track.previewUrl)

        commonButton.setOnClickListener {
            playbackControl()
        }
        binding.backButton.setNavigationOnClickListener {
            finish()
        }

        viewModel.getState().observe(this) { state ->
            playerState = state.stateData.playerState
            when (state) {
                is PlayerState.Prepared -> {
                    commonButton.isEnabled = true
                }

                is PlayerState.Completion -> {
                    handler.removeCallbacks(progressRunnable)
                    isStarted = false
                    commonButton.setImageResource(R.drawable.ic_play)
                    setText("00:00", null, binding.progress)
                }

                is PlayerState.Start -> {
                    commonButton.setImageResource(R.drawable.ic_pause)
                    isStarted = true
                    handler.postDelayed(progressRunnable, DELAY)

                }

                is PlayerState.Pause -> {
                    handler.removeCallbacks(progressRunnable)
                    isStarted = false
                    commonButton.setImageResource(R.drawable.ic_play)

                }
            }
        }
    }

    private fun setText(text: String?, key: TextView?, view: TextView?) {
        view?.text = text
        if (Objects.isNull(text) || text.isNullOrEmpty()) {
            view?.isVisible = false
            key?.isVisible = false
        }
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private var isStarted: Boolean = false

    private val progressRunnable = object : Runnable {
        override fun run() {
            setText(
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(viewModel.currentPosition()), null, binding.progress
            )
            handler.postDelayed(this, DELAY)
        }
    }

    private fun playbackControl() {
        when (playerState) {
            PlayerViewModel.STATE_PLAYING -> {
                viewModel.pausePlayer()
            }

            PlayerViewModel.STATE_PREPARED, PlayerViewModel.STATE_PAUSED -> {
                viewModel.startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {

        super.onDestroy()
        viewModel.pausePlayer()
        viewModel.releasePlayer()
    }

    companion object {
        private const val DELAY = 500L
    }
}