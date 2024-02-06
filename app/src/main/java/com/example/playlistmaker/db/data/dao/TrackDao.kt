package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.db.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Update
    fun update(track: TrackEntity)

    @Query("SELECT * FROM track_table WHERE isFavorite = 1 ORDER BY dateAdded DESC")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT EXISTS (SELECT trackId FROM track_table WHERE trackId =:trackId AND isFavorite = 1)")
    fun trackIsExists(trackId: Int): Flow<Boolean>
}