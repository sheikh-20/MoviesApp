package com.application.moviesapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_new_release_remotekey_entity")
data class MovieNewReleaseRemoteKeyEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("previous_page")
    val previousPage: Int?,

    @ColumnInfo("next_page")
    val nextPage: Int?
)