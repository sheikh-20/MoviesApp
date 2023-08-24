package com.application.moviesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.application.moviesapp.data.local.entity.MovieNewReleaseEntity
import com.application.moviesapp.data.local.entity.MovieNewReleaseRemoteKeyEntity
import com.application.moviesapp.data.local.entity.MovieRemoteKeyEntity

@Dao
interface NewReleaseRemoteKeyDao {
    @Query("SELECT * FROM movie_new_release_remotekey_entity WHERE id = :movieId")
    suspend fun getRemoteKeys(movieId: Int): MovieNewReleaseRemoteKeyEntity

    @Upsert
    suspend fun upsertAll(remoteKeys: List<MovieNewReleaseRemoteKeyEntity?>)

    @Query("DELETE FROM movie_new_release_remotekey_entity")
    suspend fun deleteAllRemoteKeys()
}