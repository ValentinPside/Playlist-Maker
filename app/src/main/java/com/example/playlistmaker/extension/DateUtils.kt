package com.example.playlistmaker.extension

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {

    fun formatTime(trackTime: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    }

    fun formatDate(releasedDate: Date): String {
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(releasedDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatString(releasedDate: String): Date {
        val formatter = DateTimeFormatter.ofPattern("yyyy")
        return LocalDate.parse(releasedDate, formatter) as Date
    }
}