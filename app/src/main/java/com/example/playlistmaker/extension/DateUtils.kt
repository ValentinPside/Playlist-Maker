package com.example.playlistmaker.extension

import android.app.Activity
import android.content.res.Configuration
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun formatTime(trackTime: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    }

    fun formatDate(releasedDate: Date): String {
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(releasedDate)
    }
}