package com.application.moviesapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.application.moviesapp.data.local.entity.MoviesEntity

@Dao
interface MoviesDao {

    @Upsert
    suspend fun upsertAll(movies: List<MoviesEntity?>)

    @Query("SELECT * FROM movies_entity")
    fun pagingSource(): PagingSource<Int, MoviesEntity>

    @Query("DELETE FROM movies_entity")
    suspend fun clearAll()
}