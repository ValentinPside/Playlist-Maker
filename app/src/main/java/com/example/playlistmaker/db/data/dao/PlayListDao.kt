package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.playlistmaker.db.data.entity.PlayListEntity
import com.example.playlistmaker.db.data.entity.PlayListWithTracks
import com.example.playlistmaker.db.data.entity.TrackWithPlayList
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {

    @Query("SELECT * FROM PlayListEntity")
    fun observeAll(): Flow<List<PlayListEntity>>

    @Query("SELECT * FROM PlayListEntity")
    fun observePlayListWithTracks(): Flow<List<PlayListWithTracks?>>

    @Insert
    suspend fun insert(playlist: PlayListEntity): Long

    @Update
    suspend fun update(playlist: PlayListEntity)

    @Transaction
    @Query("DELETE FROM PlayListEntity WHERE playListId = :playlistId")
    suspend fun delete(playlistId: Int)

    @Transaction
    @Query("DELETE FROM tracksWithPlayList WHERE playListId = :playlistId")
    suspend fun deleteAllTracksFromPlaylist(playlistId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackWithPlayList(trackWithPlayList: TrackWithPlayList)

    @Query("SELECT 1 FROM tracksWithPlayList WHERE playListId = :playListId AND trackId = :trackId")
    fun isTrackAdded(trackId: Int, playListId: Int): Int?

    @Query("SELECT * FROM PlayListEntity WHERE playListId = :playListId")
    fun getPlayListById(playListId: Int): Flow<PlayListWithTracks>

    @Query("DELETE FROM tracksWithPlayList WHERE playListId = :playlistId AND trackId = :trackId")
    suspend fun removeTrackFromPlaylist(playlistId: Int, trackId: Int)

}