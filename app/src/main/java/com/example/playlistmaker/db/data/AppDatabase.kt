package com.example.playlistmaker.db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.db.data.dao.PlayListDao
import com.example.playlistmaker.db.data.dao.TrackDao
import com.example.playlistmaker.db.data.dao.TracksPlayListDao
import com.example.playlistmaker.db.data.entity.PlayListEntity
import com.example.playlistmaker.db.data.entity.TrackEntity
import com.example.playlistmaker.db.data.entity.TrackPlayList
import com.example.playlistmaker.db.data.entity.TrackWithPlayList

@Database(
    version = 1,
    entities = [
        TrackEntity::class,
        PlayListEntity::class,
        TrackPlayList::class,
        TrackWithPlayList::class
    ]
)
@TypeConverters(
    DateConverter::class,
    UriConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playListDao(): PlayListDao

    abstract fun tracksPlayListDao(): TracksPlayListDao

}