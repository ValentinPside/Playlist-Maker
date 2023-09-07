package com.example.playlistmaker

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

    fun isDarkTheme(activity: Activity): Boolean {
        return activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}