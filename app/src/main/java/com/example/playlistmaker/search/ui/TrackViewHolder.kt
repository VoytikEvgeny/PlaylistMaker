package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.CLICKED_TRACK_CONTENT
import com.example.playlistmaker.R
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(
    parent: ViewGroup,
    trackList: List<Track>,
    private val searchHistoryService: SearchHistoryService,
    private val gson: Gson
) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.activity_track, parent, false)
    ) {
    private val trackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val trackIcon: ImageView = itemView.findViewById(R.id.ivArtwork)
    private val trackArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val trackTime: TextView = itemView.findViewById(R.id.tvTrackTime)

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    init {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION && clickDebounce()) {
                val clickedItem = trackList[position]
                showPlayerActivity(itemView.context, clickedItem)
                searchHistoryService.addToHistory(clickedItem)
            }
        }
    }

    private fun showPlayerActivity(context: Context?, clickedItem: Track) {
        val playerIntent = Intent(context, PlayerActivity::class.java)
            .putExtra(CLICKED_TRACK_CONTENT, gson.toJson(clickedItem))

        context?.startActivity(playerIntent)
    }

    fun bind(model: Track) {
        trackName.text = model.trackName
        trackArtistName.text = ""
        trackArtistName.text = model.artistName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .fitCenter()
            .placeholder(R.drawable.ic_placeholder_45)
            .transform(RoundedCorners(dpToPx(2.0f, itemView.context)))
            .into(trackIcon)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}