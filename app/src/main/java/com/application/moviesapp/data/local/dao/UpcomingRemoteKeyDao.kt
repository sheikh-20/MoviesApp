package com.application.moviesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.application.moviesapp.data.local.entity.MovieUpcomingRemoteKeyEntity

@Dao
interface UpcomingRemoteKeyDao {
    @Query("SELECT * FROM movie_upcoming_remotekey_entity WHERE id = :movieId")
    suspend fun getRemoteKeys(movieId: Int): MovieUpcomingRemoteKeyEntity

    @Upsert
    suspend fun upsertAll(remoteKeys: List<MovieUpcomingRemoteKeyEntity?>)

    @Query("DELETE FROM movie_upcoming_remotekey_entity")
    suspend fun deleteAllRemoteKeys()
}