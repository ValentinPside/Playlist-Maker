package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlayListsBottomSheetFragment: BottomSheetDialogFragment() {

    //private val binding by viewBinding(BottomSheetPlayListBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_play_list, container, false)
        return view
    }

//    override fun getTheme(): Int {
//        return R.style.ModalBottomSheetDialog
//    }

}