package com.application.moviesapp.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvSeriesEpisodesDto(
    @SerialName("_id")
    val fetchId: String?,

    @SerialName("air_date")
    val airDate: String?,

    @SerialName("episodes")
    val episodes: List<Episode?>?,

    @SerialName("id")
    val id: Int?,

    @SerialName("name")
    val name: String?,

    @SerialName("overview")
    val overview: String?,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("season_number")
    val seasonNumber: Int?,

    @SerialName("vote_average")
    val voteAverage: Double?
) {

    @Serializable
    data class Episode(
        @SerialName("air_date")
        val airDate: String?,

        @SerialName("episode_number")
        val episodeNumber: Int?,

        @SerialName("episode_type")
        val episodeType: String?,

        @SerialName("guest_stars")
        val guestStars: List<GuestStar?>?,

        @SerialName("id")
        val id: Int?,

        @SerialName("name")
        val name: String?,

        @SerialName("overview")
        val overview: String?,

        @SerialName("production_code")
        val productionCode: String?,

        @SerialName("runtime")
        val runtime: Int?,

        @SerialName("season_number")
        val seasonNumber: Int?,

        @SerialName("show_id")
        val showId: Int?,

        @SerialName("still_path")
        val stillPath: String?,

        @SerialName("vote_average")
        val voteAverage: Double?,

        @SerialName("vote_count")
        val voteCount: Int?
    ) {

        @Serializable
        data class GuestStar(

            @SerialName("adult")
            val adult: Boolean?,

            @SerialName("character")
            val character: String?,

            @SerialName("credit_id")
            val creditId: String?,

            @SerialName("gender")
            val gender: Int?,

            @SerialName("id")
            val id: Int?,

            @SerialName("known_for_department")
            val knownForDepartment: String?,

            @SerialName("name")
            val name: String?,

            @SerialName("order")
            val order: Int?,

            @SerialName("original_name")
            val originalName: String?,

            @SerialName("popularity")
            val popularity: Double?,

            @SerialName("profile_path")
            val profilePath: String?
        )
    }
}