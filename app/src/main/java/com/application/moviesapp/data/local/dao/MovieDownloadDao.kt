package com.application.moviesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDownloadDao {
    @Query("SELECT * FROM movie_download_entity WHERE title LIKE :search || '%' ORDER BY update_at DESC")
    fun getAllDownloads(search: String = ""): Flow<List<MovieDownloadEntity>>

    @Insert
    suspend fun insertDownload(download: MovieDownloadEntity)

    @Delete
    suspend fun deleteDownload(download: MovieDownloadEntity)
}