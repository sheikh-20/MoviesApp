package com.application.moviesapp.domain.model

data class CastTvSeriesCredit(

    val cast: List<Cast?>?,

    val crew: List<Crew?>?,

    val id: Int?
) {

    data class Cast(

        val adult: Boolean?,

        val backdropPath: String?,

        val character: String?,

        val creditId: String?,

        val episodeCount: Int?,

        val firstAirDate: String?,

        val genreIds: List<Int?>?,

        val id: Int?,

        val name: String?,

        val originCountry: List<String?>?,

        val originalLanguage: String?,

        val originalName: String?,

        val overview: String?,

        val popularity: Double?,

        val posterPath: String?,

        val voteAverage: Double?,

        val voteCount: Int?
    )

    data class Crew(

        val adult: Boolean?,

        val backdropPath: String?,

        val creditId: String?,

        val department: String?,

        val episodeCount: Int?,

        val firstAirDate: String?,

        val genreIds: List<Int?>?,

        val id: Int?,

        val job: String?,

        val name: String?,

        val originCountry: List<String?>?,

        val originalLanguage: String?,

        val originalName: String?,

        val overview: String?,

        val popularity: Double?,

        val posterPath: String?,

        val voteAverage: Double?,

        val voteCount: Int?
    )
}