package com.application.moviesapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.application.moviesapp.data.local.entity.MovieNowPlayingEntity

@Dao
interface MovieNowPlayingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(movies: List<MovieNowPlayingEntity?>)

    @Query("SELECT * FROM movie_now_playing_entity")
    fun pagingSource(): PagingSource<Int, MovieNowPlayingEntity>

    @Query("DELETE FROM movie_now_playing_entity")
    suspend fun clearAll()
}