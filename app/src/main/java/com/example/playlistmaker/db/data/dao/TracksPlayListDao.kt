package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.playlistmaker.db.data.entity.TrackPlayList

@Dao
interface TracksPlayListDao {

    @Insert
    suspend fun insert(trackPlayList: TrackPlayList)

}