package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.search.domain.ItemClickListener
import com.example.playlistmaker.search.domain.models.Track

class TrackListAdapter : RecyclerView.Adapter<TrackListViewHolder>(), ItemClickListener {
    var trackList = mutableListOf<Track>()
    override var onItemClick: ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackListViewHolder(ActivityTrackBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(trackList[position])
        }
    }

    fun removeItems() {
        trackList.clear()
    }
}