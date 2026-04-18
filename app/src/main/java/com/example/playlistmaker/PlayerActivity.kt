package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Objects

class PlayerActivity : AppCompatActivity() {
    val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.back_button)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val trackJson = intent.getStringExtra(CLICKED_TRACK)
        val track = gson.fromJson(trackJson, Track::class.java)

        val trackIcon = findViewById<ImageView>(R.id.cover)
        Glide.with(this)
            .load(getCoverArtwork(track))
            .fitCenter()
            .placeholder(R.drawable.ic_placeholder_45)
            .transform(RoundedCorners(dpToPx(8.0f, this)))
            .into(trackIcon)
        val durability = findViewById<TextView>(R.id.durability_val)
        durability.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        setText("1:23", null, findViewById(R.id.progress))
        setText(track.trackName, null, findViewById(R.id.track_name))
        setText(track.artistName, null, findViewById(R.id.artist_name))
        setText(track.collectionName, findViewById(R.id.album), findViewById(R.id.album_val))
        setText(track.primaryGenreName, findViewById(R.id.genre), findViewById(R.id.genre_val))
        setText(track.country, findViewById(R.id.country), findViewById(R.id.country_val))

        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = inputFormat.parse(track.releaseDate)
            val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            setText(
                outputFormat.format(date!!),
                findViewById(R.id.year),
                findViewById(R.id.year_val)
            )
        } catch (exc: Exception) {
            setText(
                "",
                findViewById(R.id.year),
                findViewById(R.id.year_val)
            )
        }
    }

    private fun getCoverArtwork(track: Track): String {
        return track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun setText(text: String, key: TextView?, view: TextView?) {
        view?.text = text
        if (Objects.isNull(text) || text.isEmpty()) {
            view?.visibility = View.GONE
            key?.visibility = View.GONE
        }
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

}