package com.example.playlistmaker.db.data.dao

import androidx.room.*
import com.example.playlistmaker.db.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Delete
    fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY dateAdded DESC")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT EXISTS (SELECT trackId FROM track_table WHERE trackId =:trackId)")
    fun trackIsExists(trackId: Int): Flow<Boolean>
}