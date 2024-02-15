package com.application.moviesapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class MovieSearch(
    val adult: Boolean?,

    val backdropPath: String?,

    val genreIds: List<Int?>?,

    val id: Int?,

    val originalLanguage: String?,

    val originalTitle: String?,

    val overview: String?,

    val popularity: Double?,

    val posterPath: String?,

    val releaseDate: String?,

    val title: String?,

    val video: Boolean?,

    val voteAverage: Double?,

    val voteCount: Int?
)