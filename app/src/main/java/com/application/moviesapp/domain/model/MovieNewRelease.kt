package com.application.moviesapp.domain.model

data class MovieNewRelease(
    val adult: Boolean?,

    val backdropPath: String?,

    val id: Int?,

    val movieId: Int?,

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