package com.example.playlistmaker.search.ui

import android.icu.text.SimpleDateFormat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.dpToPx
import java.util.Locale

class TrackListViewHolder(private val binding: ActivityTrackBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Track) {
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(binding.ivArtwork)
        binding.tvTrackName.text = item.trackName
        binding.tvArtistName.text = item.artistName
        binding.tvTrackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis.toLong())
        binding.tvArtistName.requestLayout()
    }
}