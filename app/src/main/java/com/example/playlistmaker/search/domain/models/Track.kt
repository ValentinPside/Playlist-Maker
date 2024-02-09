package com.example.playlistmaker.search.domain.models

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Track(
    val trackName: String?,
    val releaseDate: Date?,
    val collectionName: String?,
    val primaryGenreName: String?,
    val country: String?,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val trackId: Int,
    val previewUrl : String?,
    var isFavorite: Boolean = false )
    : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readSerializable() as Date?,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt() == 1
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackName)
        parcel.writeSerializable(releaseDate)
        parcel.writeString(collectionName)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
        parcel.writeString(artistName)
        if (trackTimeMillis != null) {
            parcel.writeLong(trackTimeMillis)
        }
        parcel.writeString(artworkUrl100)
        parcel.writeInt(trackId)
        parcel.writeString(previewUrl)
        parcel.writeInt( if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}