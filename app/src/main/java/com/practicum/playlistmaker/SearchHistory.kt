package com.practicum.playlistmaker

import android.content.SharedPreferences

class SearchHistory(val sharedPreferences: App) {

    private companion object {
        const val MAX_TRACKS_LIST_COUNT = 10
    }

    val tracks: ArrayList<Track> = getArrayOfTracks()

    private fun getArrayOfTracks(): ArrayList<Track> {
        return sharedPreferences.readSearchHistory()
    }

    fun addTrack(track: Track) {
        if (tracks.isEmpty()) {
            tracks.add(track)
            sharedPreferences.writeSearchHistory(tracks)
            return
        }
        if (tracks.isNotEmpty()) {
            for (item in tracks) {
                if (item.key == track.key) {
                    tracks.remove(item)
                    tracks.add(0, track)
                    sharedPreferences.writeSearchHistory(tracks)
                    return
                }
            }
        }
        if (tracks.size < MAX_TRACKS_LIST_COUNT) {
            tracks.add(0, track)
        } else {
            tracks.removeLast()
            tracks.add(0, track)
        }

        sharedPreferences.writeSearchHistory(tracks)
    }

    fun getTracksHistory(): ArrayList<Track> {
        return tracks
    }

    fun clean() {
        tracks.clear()
        sharedPreferences.writeSearchHistory(tracks)
    }

}