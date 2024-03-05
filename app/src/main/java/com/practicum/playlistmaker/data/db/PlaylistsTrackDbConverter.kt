package com.practicum.playlistmaker.data.db

import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.domain.search.models.Track

class PlaylistsTrackDbConverter {

    fun map(playList: PlayList): PlayListEntity {
        playList.apply {
            return PlayListEntity(
                null,
                name,
                description,
                image
            )
        }
    }

    fun map(playListWithCountTracks: PlayListWithCountTracks): PlayList {
        playListWithCountTracks.apply {
            return PlayList(
                playListId!!,
                name,
                description,
                image,
                tracksCount,
            )
        }
    }

    fun map(playListsTrackEntity: PlayListsTrackEntity): Track {
        playListsTrackEntity.apply {
            return Track(
                trackId,
                trackName,
                artistName,
                trackTimeMillis,
                artworkUrl100,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl
            )
        }
    }

    fun map(track: Track): PlayListsTrackEntity {
        track.apply {
            return PlayListsTrackEntity(
                trackId,
                trackName,
                artistName,
                trackTimeMillis,
                artworkUrl100,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl,
            )
        }
    }

}