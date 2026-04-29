package com.example.playlistmaker.presentation.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.ui.search.DELAYED

class PlayerActivity : AppCompatActivity() {

    private lateinit var pauseButton: ImageButton
    private val trackManager = Creator.getTrackManager()
    private val handler = Handler(Looper.getMainLooper())
    private val player = Creator.getMediaPlayer()
    private lateinit var timerRunnable: Runnable
    private lateinit var tvTimeUnderPause: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val track = Creator.getMapper().map(trackManager.getLastTrack())
        timerRunnable = timerManager()

        player.prepare(track.previewUrl, timerRunnable)


        val toolbar = findViewById<Toolbar>(R.id.back_button)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        pauseButton = findViewById(R.id.common_button)
        val tvTrackName = findViewById<TextView>(R.id.track_name)
        val tvPoster = findViewById<ImageView>(R.id.cover)
        val tvArtistName = findViewById<TextView>(R.id.artist_name)
        val tvTrackTime = findViewById<TextView>(R.id.progress)
        tvTimeUnderPause = findViewById(R.id.progress)
        val tvYear = findViewById<TextView>(R.id.year_val)
        val tvGenre = findViewById<TextView>(R.id.genre_val)
        val tvCountry = findViewById<TextView>(R.id.country_val)
        val tvAlbum = findViewById<TextView>(R.id.album_val)
        val tvDurability = findViewById<TextView>(R.id.durability_val)
        val artworkUrlHR: String = track.getHighResArtworkUrl()

        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        tvTrackTime.text = track.getFormattedTime()
        tvYear.text = track.getReleaseYear()
        tvGenre.text = track.primaryGenreName
        tvCountry.text = track.country
        tvAlbum.text = track.collectionName
        tvDurability.text = track.getFormattedTime()
        pauseButton.isEnabled = true
        pauseButton.setImageResource(R.drawable.ic_play)
        tvTimeUnderPause.text = formatTime(30)


        Glide.with(this)
            .load(artworkUrlHR)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerInside()
            .transform(RoundedCorners(8))
            .into(tvPoster)


        pauseButton.setOnClickListener {
            player.playbackControl(timerRunnable)
            buttonAppearance()
        }

    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.destroy()
    }

    private fun buttonAppearance() {
        if (player.getCurrentState()) {
            pauseButton.setImageResource(R.drawable.ic_pause)
        } else {
            pauseButton.setImageResource(R.drawable.ic_play)
        }
    }

    private fun timerManager(): Runnable {
        return object : Runnable {
            override fun run() {
                val currentSeconds = player.getSecondsRemain()
                tvTimeUnderPause.text = formatTime(currentSeconds)

                if (currentSeconds > 0 && player.getCurrentState()) {
                    handler.postDelayed(this, DELAYED)
                } else if (!player.getCurrentState()) {
                    tvTimeUnderPause.text = formatTime(currentSeconds)
                    pauseButton.setImageResource(R.drawable.ic_play)
                } else {
                    tvTimeUnderPause.text = formatTime(30)
                    pauseButton.setImageResource(R.drawable.ic_play)
                }
            }
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }


}