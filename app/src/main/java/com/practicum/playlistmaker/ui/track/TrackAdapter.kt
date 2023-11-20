package com.practicum.playlistmaker.ui.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.search.IClickView
import com.practicum.playlistmaker.domain.search.models.Track

class TrackAdapter(private val track: List<Track>/*, IClickView*/) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount() = track.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        /*holder.bind(track[position], listener)
        holder.itemView.setOnClickListener {
            listener.onClick(track[position])
        }*/
    }
}
