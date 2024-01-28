package com.example.playlistmaker.uttils

object StringUtils {

    fun getTracksAddition(count: Int): String {
        val preLastDigit = count % 100 / 10
        if (preLastDigit == 1) {
            return "треков"
        }

        return when (count % 10) {
            1 -> "трек"
            2,3,4 -> "трека"
            else -> "треков"
        }
    }
}