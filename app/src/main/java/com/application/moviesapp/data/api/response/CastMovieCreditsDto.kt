package com.application.moviesapp.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CastMovieCreditsDto(

    @SerialName("cast")
    val cast: List<Cast?>?,

    @SerialName("crew")
    val crew: List<Crew?>?,

    @SerialName("id")
    val id: Int?
) {

    @Serializable
    data class Cast(
        @SerialName("adult")
        val adult: Boolean?,

        @SerialName("backdrop_path")
        val backdropPath: String?,

        @SerialName("character")
        val character: String?,

        @SerialName("credit_id")
        val creditId: String?,

        @SerialName("genre_ids")
        val genreIds: List<Int?>?,

        @SerialName("id")
        val id: Int?,

        @SerialName("order")
        val order: Int?,

        @SerialName("original_language")
        val originalLanguage: String?,

        @SerialName("original_title")
        val originalTitle: String?,

        @SerialName("overview")
        val overview: String?,

        @SerialName("popularity")
        val popularity: Double?,

        @SerialName("poster_path")
        val posterPath: String?,

        @SerialName("release_date")
        val releaseDate: String?,

        @SerialName("title")
        val title: String?,

        @SerialName("video")
        val video: Boolean?,

        @SerialName("vote_average")
        val voteAverage: Double?,

        @SerialName("vote_count")
        val voteCount: Int?
    )

    @Serializable
    data class Crew(
        @SerialName("adult")
        val adult: Boolean?,

        @SerialName("backdrop_path")
        val backdropPath: String?,

        @SerialName("credit_id")
        val creditId: String?,

        @SerialName("department")
        val department: String?,

        @SerialName("genre_ids")
        val genreIds: List<Int?>?,

        @SerialName("id")
        val id: Int?,

        @SerialName("job")
        val job: String?,

        @SerialName("original_language")
        val originalLanguage: String?,

        @SerialName("original_title")
        val originalTitle: String?,

        @SerialName("overview")
        val overview: String?,

        @SerialName("popularity")
        val popularity: Double?,

        @SerialName("poster_path")
        val posterPath: String?,

        @SerialName("release_date")
        val releaseDate: String?,

        @SerialName("title")
        val title: String?,

        @SerialName("video")
        val video: Boolean?,

        @SerialName("vote_average")
        val voteAverage: Double?,

        @SerialName("vote_count")
        val voteCount: Int?
    )
}