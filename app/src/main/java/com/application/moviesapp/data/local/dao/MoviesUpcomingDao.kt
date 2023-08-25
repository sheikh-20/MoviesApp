package com.application.moviesapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.application.moviesapp.data.local.entity.MovieNewReleaseEntity
import com.application.moviesapp.data.local.entity.MovieUpcomingEntity

@Dao
interface MoviesUpcomingDao {
    @Upsert
    suspend fun upsertAll(movies: List<MovieUpcomingEntity?>)

    @Query("SELECT * FROM movie_upcoming_entity")
    fun pagingSource(): PagingSource<Int, MovieUpcomingEntity>

    @Query("DELETE FROM movie_upcoming_entity")
    suspend fun clearAll()
}