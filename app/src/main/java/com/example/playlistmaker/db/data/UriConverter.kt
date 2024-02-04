package com.example.playlistmaker.db.data

import android.net.Uri
import androidx.room.TypeConverter

object UriConverter {
    @TypeConverter
    fun toUri(text: String?): Uri? {
        return text?.let { Uri.parse(it) }
    }

    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }
}