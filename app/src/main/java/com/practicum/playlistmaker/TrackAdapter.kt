package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val track: List<Track>, private val listener: IClickView) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount() = track.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position], listener)
        holder.itemView.setOnClickListener {
            listener.onClick(track[position])
        }
    }
}
