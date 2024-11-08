package com.application.moviesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.application.moviesapp.data.local.entity.MovieNowPlayingRemoteKeyEntity

@Dao
interface MovieNowPlayingRemoteKeyDao {
    @Query("SELECT * FROM movie_now_playing_remotekey_entity WHERE movie_id = :movieId")
    suspend fun getRemoteKeys(movieId: Int): MovieNowPlayingRemoteKeyEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(remoteKeys: List<MovieNowPlayingRemoteKeyEntity>)

    @Query("DELETE FROM movie_now_playing_remotekey_entity")
    suspend fun deleteAllRemoteKeys()
}