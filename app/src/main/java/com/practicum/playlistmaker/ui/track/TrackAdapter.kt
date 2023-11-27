package com.practicum.playlistmaker.ui.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.search.models.Track

class TrackAdapter(private var clickListener: LocationClickListener?): RecyclerView.Adapter<TrackViewHolder>() {
    fun interface LocationClickListener {
        fun onLocationClick(track: Track)
    }

    private val listTrack = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTrack.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
       holder.bind(listTrack[position])
       holder.itemView.setOnClickListener {
           clickListener?.onLocationClick(listTrack[position])
       }
    }

    fun setTracks(tracks: List<Track>?) {
        listTrack.clear()
        if (tracks != null) {
            listTrack.addAll(tracks)
        }
    }

}
