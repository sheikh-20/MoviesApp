package com.application.moviesapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_upcoming_remotekey_entity")
data class MovieUpcomingRemoteKeyEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("previous_page")
    val previousPage: Int?,

    @ColumnInfo("next_page")
    val nextPage: Int?
)
