package com.practicum.playlistmaker.ui.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.search.models.Track

class TrackAdapter(private var clickListener: LocationClickListener?,
                   private val longClickListener: LongTrackClickListener? = null) :
    RecyclerView.Adapter<TrackViewHolder>() {
    fun interface LocationClickListener {
        fun onLocationClick(track: Track)
    }

    fun interface LongTrackClickListener {
        fun onTrackLongClick(track: Track)
    }


    var listTrack = ArrayList<Track>()

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
        longClickListener?.let { listener ->
            holder.itemView.setOnLongClickListener {
                listener.onTrackLongClick(listTrack[holder.adapterPosition])
                return@setOnLongClickListener true
            }
        }
    }


    fun setTracks(tracks: List<Track>?) {
        clearTracks()
        if (tracks != null) {
            listTrack.addAll(tracks)
        }
    }

    fun clearTracks() {
        listTrack.clear()
    }

}
