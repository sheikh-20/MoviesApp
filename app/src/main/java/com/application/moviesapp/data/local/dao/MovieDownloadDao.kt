package com.application.moviesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDownloadDao {

    @Query("SELECT * from movie_download_entity ORDER BY update_at DESC")
    fun getAllDownloads(): Flow<List<MovieDownloadEntity>>

    @Insert
    suspend fun insertDownload(download: MovieDownloadEntity)

    @Delete
    suspend fun deleteDownload(download: MovieDownloadEntity)
}