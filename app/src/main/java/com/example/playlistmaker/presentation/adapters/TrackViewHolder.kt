package com.example.playlistmaker.presentation.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //    val itemView: Any
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)
    private val icSong: ImageView = itemView.findViewById(R.id.ivArtwork)

    fun bind(item: Track) {
        val artworkUrl: String = item.artworkUrl100

        Glide.with(itemView)
            .load(artworkUrl)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerInside()
            .transform(RoundedCorners(2))
            .into(icSong)

        tvTrackTime.text = item.getFormattedTime()
        tvTrackName.text = item.trackName.trim()
        tvArtistName.text = item.artistName.trim()
    }


}