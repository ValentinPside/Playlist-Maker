package com.example.playlistmaker.db.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.playlistmaker.db.data.entity.TrackEntity
import com.example.playlistmaker.extension.DateUtils
import com.example.playlistmaker.search.domain.models.Track
import java.util.*

class TrackDbConvertor {

    fun map(track: Track): TrackEntity {
        return TrackEntity(track.trackId, track.artworkUrl100, track.trackName, track.artistName,
            track.collectionName,
            track.releaseDate, track.primaryGenreName, track.country,
            track.trackTimeMillis, track.previewUrl)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun map(track: TrackEntity): Track {
        return Track(track.trackName,
            track.releaseDate, track.collectionName, track.primaryGenreName,
            track.country, track.artistName, track.trackTimeMillis, track.artworkUrl100,
            track.trackId, track.previewUrl)
    }

}