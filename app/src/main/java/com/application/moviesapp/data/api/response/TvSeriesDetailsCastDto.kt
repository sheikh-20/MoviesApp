package com.application.moviesapp.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvSeriesDetailsCastDto(
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
        val profilePath: String?,

        @SerialName("roles")
        val roles: List<Role?>?,

        @SerialName("total_episode_count")
        val totalEpisodeCount: Int?
    ) {

        @Serializable
        data class Role(
            @SerialName("character")
            val character: String?,

            @SerialName("credit_id")
            val creditId: String?,

            @SerialName("episode_count")
            val episodeCount: Int?
        )
    }

    @Serializable
    data class Crew(
        @SerialName("adult")
        val adult: Boolean?,

        @SerialName("department")
        val department: String?,

        @SerialName("gender")
        val gender: Int?,

        @SerialName("id")
        val id: Int?,

        @SerialName("jobs")
        val jobs: List<Job?>?,

        @SerialName("known_for_department")
        val knownForDepartment: String?,

        @SerialName("name")
        val name: String?,

        @SerialName("original_name")
        val originalName: String?,

        @SerialName("popularity")
        val popularity: Double?,

        @SerialName("profile_path")
        val profilePath: String?,

        @SerialName("total_episode_count")
        val totalEpisodeCount: Int?
    ) {

        @Serializable
        data class Job(
            @SerialName("credit_id")
            val creditId: String?,

            @SerialName("episode_count")
            val episodeCount: Int?,

            @SerialName("job")
            val job: String?
        )
    }
}