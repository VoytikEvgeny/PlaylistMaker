package com.example.playlistmaker.presentation.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.ui.player.PlayerActivity

class TrackHistoryAdapter(
    private val context: Context, private var trackHistory: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {

    val history = Creator.getTrackManager()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackHistory[position])

        holder.itemView.setOnClickListener {
            history.putLastTrack(Creator.getMapper().reversedMap(trackHistory[position]))
            val displayIntent = Intent(context, PlayerActivity::class.java)
            context.startActivity(displayIntent)
        }

    }

    fun updateData(newData: List<Track>) {
        trackHistory = newData
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return trackHistory.size
    }
}