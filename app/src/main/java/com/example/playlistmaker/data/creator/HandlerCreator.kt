package com.example.playlistmaker.data.creator

import android.os.Handler
import android.os.Looper

object HandlerCreator {
    fun createHandler() : Handler {
        return Handler(Looper.getMainLooper())
    }
}