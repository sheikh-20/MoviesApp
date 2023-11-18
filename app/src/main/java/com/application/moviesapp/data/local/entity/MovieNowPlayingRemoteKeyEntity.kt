package com.application.moviesapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_now_playing_remotekey_entity")
data class MovieNowPlayingRemoteKeyEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("movie_id")
    val movieId: Int,

    @ColumnInfo("previous_page")
    val previousPage: Int?,

    @ColumnInfo("next_page")
    val nextPage: Int?,

    @ColumnInfo("current_page")
    val currentPage: Int,
)