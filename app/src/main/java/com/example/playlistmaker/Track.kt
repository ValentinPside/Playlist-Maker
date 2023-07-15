package com.example.playlistmaker

class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: Long)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val track = other as Track
        return trackId == track.trackId
    }

    override fun hashCode(): Int {
        return trackId.hashCode()
    }
}
