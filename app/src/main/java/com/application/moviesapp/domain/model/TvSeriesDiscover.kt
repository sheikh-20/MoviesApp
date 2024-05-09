package com.application.moviesapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TvSeriesDiscover(
    val adult: Boolean?,

    val backdropPath: String?,

    val id: Int?,

    val originalLanguage: String?,

    val originalName: String?,

    val overview: String?,

    val popularity: Double?,

    val posterPath: String?,

    val firstAirDate: String?,

    val name: String?,

    val voteAverage: Double?,

    val voteCount: Int?
)

