package com.application.moviesapp.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CastTvSeriesCreditDto(

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

        @SerialName("episode_count")
        val episodeCount: Int?,

        @SerialName("first_air_date")
        val firstAirDate: String?,

        @SerialName("genre_ids")
        val genreIds: List<Int?>?,

        @SerialName("id")
        val id: Int?,

        @SerialName("name")
        val name: String?,

        @SerialName("origin_country")
        val originCountry: List<String?>?,

        @SerialName("original_language")
        val originalLanguage: String?,

        @SerialName("original_name")
        val originalName: String?,

        @SerialName("overview")
        val overview: String?,

        @SerialName("popularity")
        val popularity: Double?,

        @SerialName("poster_path")
        val posterPath: String?,

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

        @SerialName("episode_count")
        val episodeCount: Int?,

        @SerialName("first_air_date")
        val firstAirDate: String?,

        @SerialName("genre_ids")
        val genreIds: List<Int?>?,

        @SerialName("id")
        val id: Int?,

        @SerialName("job")
        val job: String?,

        @SerialName("name")
        val name: String?,

        @SerialName("origin_country")
        val originCountry: List<String?>?,

        @SerialName("original_language")
        val originalLanguage: String?,

        @SerialName("original_name")
        val originalName: String?,

        @SerialName("overview")
        val overview: String?,

        @SerialName("popularity")
        val popularity: Double?,

        @SerialName("poster_path")
        val posterPath: String?,

        @SerialName("vote_average")
        val voteAverage: Double?,

        @SerialName("vote_count")
        val voteCount: Int?
    )
}