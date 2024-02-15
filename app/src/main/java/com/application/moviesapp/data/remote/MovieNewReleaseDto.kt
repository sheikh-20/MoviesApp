package com.application.moviesapp.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieNewReleasesDto(
    @SerialName("dates")
    val dates: Dates?,

    @SerialName("page")
    val page: Int?,

    @SerialName("results")
    val results: List<Result?>?,

    @SerialName("total_pages")
    val totalPages: Int?,

    @SerialName("total_results")
    val totalResults: Int?
) {
    @Serializable
    data class Dates(
        @SerialName("maximum")
        val maximum: String?,

        @SerialName("minimum")
        val minimum: String?
    )

    @Serializable
    data class Result(
        @SerialName("adult")
        val adult: Boolean?,

        @SerialName("backdrop_path")
        val backdropPath: String?,

        @SerialName("genre_ids")
        val genreIds: List<Int?>?,

        @SerialName("id")
        val id: Int?,

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