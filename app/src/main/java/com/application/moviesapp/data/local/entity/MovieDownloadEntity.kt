package com.application.moviesapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "movie_download_entity")
data class MovieDownloadEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,

    @ColumnInfo(name = "runtime")
    val runtime: String?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "file_path")
    val filePath: String?,

    @ColumnInfo(name = "update_at")
    val updatedAt: Long? = Calendar.getInstance().timeInMillis
)