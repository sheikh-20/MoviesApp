package com.application.moviesapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.application.moviesapp.data.local.entity.MovieNewReleaseEntity
import com.application.moviesapp.data.local.entity.MoviesEntity
import com.application.moviesapp.domain.model.MovieNewRelease

@Dao
interface MoviesNewReleaseDao {
    @Upsert
    suspend fun upsertAll(movies: List<MovieNewReleaseEntity?>)

    @Query("SELECT * FROM movie_new_release_entity")
    fun pagingSource(): PagingSource<Int, MovieNewReleaseEntity>

    @Query("DELETE FROM movie_new_release_entity")
    suspend fun clearAll()
}