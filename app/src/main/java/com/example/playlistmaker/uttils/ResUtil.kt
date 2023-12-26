package com.example.playlistmaker.uttils

import android.content.Context
import android.content.res.Resources.Theme
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat


object ResUtil {
    fun getDrawableRes(c: Context, @AttrRes attr: Int): Int {
        return getDrawableRes(c.theme, attr)
    }

    fun getDrawableRes(theme: Theme, @AttrRes attr: Int): Int {
        val out = TypedValue()
        theme.resolveAttribute(attr, out, true)
        return out.resourceId
    }

    fun getDrawable(c: Context, @AttrRes attr: Int): Drawable? {
        return ContextCompat.getDrawable(c, getDrawableRes(c, attr))
    }
}