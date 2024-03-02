package com.practicum.playlistmaker.ui.playlist

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.search.models.PlayList
import java.io.File

class PlayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val playListImage: ImageView = itemView.findViewById(R.id.iv_cover_playlist)
    private val playListName: TextView = itemView.findViewById(R.id.tv_playlist_name)
    private val playListCountTracks: TextView = itemView.findViewById(R.id.tv_playlist_tracks_count)

    fun bind(playList: PlayList) {
        playListName.text = playList.name
        playListCountTracks.text = playListCountTracks.resources.getQuantityString(
            R.plurals.plural_count_tracks, playList.tracksCount, playList.tracksCount
        )
        val filePath = File(
            itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            Consts.PLAY_LISTS_IMAGES_DIRECTORY
        )

        Glide
            .with(itemView)
            .load(playList.image?.let { imageName -> File(filePath, imageName) })
            .placeholder(R.drawable.track_pic_312)
            .into(playListImage)
    }
}