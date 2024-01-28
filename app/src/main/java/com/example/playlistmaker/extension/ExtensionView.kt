package com.example.playlistmaker.extension

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

fun View.visibleOrGone(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleOrInvisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun EditText.onTextChanged(callback: (text: String) -> Unit) {

    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            callback.invoke(text?.toString().orEmpty())
        }

        override fun afterTextChanged(p0: Editable?) {}

    })

}