package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(
    itemView: View, trackList: List<Track>,
    private val searchHistory: SearchHistory
) : RecyclerView.ViewHolder(itemView) {
    private val ivArtwork: ImageView = itemView.findViewById(R.id.ivArtwork)
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    init {
        itemView.setOnClickListener {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION && clickDebounce()) {
                val clickedItem = trackList[position]
                showPlayerActivity(itemView.context, clickedItem)
                searchHistory.addToHistory(clickedItem)
            }
        }
    }

    private fun showPlayerActivity(context: Context?, clickedItem: Track) {
        val playerIntent = Intent(context, PlayerActivity::class.java)
            .putExtra(CLICKED_TRACK, clickedItem)

        context?.startActivity(playerIntent)
    }

    fun bind(item: Track) {

        tvTrackName.text = item.trackName
        tvArtistName.text = ""
        tvArtistName.text = item.artistName
        tvTrackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)

        Glide.with(itemView.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder_45)
            .error(R.drawable.ic_placeholder_45)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(dpToPx(8.0f, itemView.context))))
            .fitCenter()
            .into(ivArtwork)
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