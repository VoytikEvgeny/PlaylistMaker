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

class TrackAdapter(
    private val context: Context, private val track: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {

    private val history = Creator.getTrackManager()
    private val mapper = Creator.getMapper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position])

        holder.itemView.setOnClickListener {
            history.addTrackToHistory(mapper.reversedMap(track[position]))
            Creator.getSharedPrefs().saveHistory(history.getTrackHistory(), holder.itemView.context)
            history.putLastTrack(mapper.reversedMap(track[position]))
            val displayIntent = Intent(context, PlayerActivity::class.java)
            context.startActivity(displayIntent)
        }
    }


    override fun getItemCount(): Int {
        return track.size
    }

}