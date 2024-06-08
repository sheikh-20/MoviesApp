package com.application.moviesapp.domain.model

data class CastMovieCredits(
    val cast: List<Cast?>?,
    val crew: List<Crew?>?,
    val id: Int?
) {

    data class Cast(
        val adult: Boolean?,

        val backdropPath: String?,

        val character: String?,

        val creditId: String?,

        val genreIds: List<Int?>?,

        val id: Int?,

        val order: Int?,

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

    data class Crew(
        val adult: Boolean?,

        val backdropPath: String?,

        val creditId: String?,

        val department: String?,

        val genreIds: List<Int?>?,

        val id: Int?,

        val job: String?,

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
}