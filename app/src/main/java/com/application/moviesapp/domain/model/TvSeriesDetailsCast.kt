package com.application.moviesapp.domain.model

import com.application.moviesapp.data.api.response.TvSeriesDetailsCastDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TvSeriesDetailsCast(
    val cast: List<Cast?>?,

    val crew: List<Crew?>?,

    val id: Int?
) {
    data class Cast(

        val adult: Boolean?,

        val gender: Int?,

        val id: Int?,

        val knownForDepartment: String?,

        val name: String?,

        val order: Int?,

        val originalName: String?,

        val popularity: Double?,

        val profilePath: String?,

        val roles: List<Role?>?,

        val totalEpisodeCount: Int?
    ) {

        data class Role(
            val character: String?,

            val creditId: String?,

            val episodeCount: Int?
        )
    }

    data class Crew(

        val adult: Boolean?,

        val department: String?,

        val gender: Int?,

        val id: Int?,

        val jobs: List<Job?>?,

        val knownForDepartment: String?,

        val name: String?,

        val originalName: String?,

        val popularity: Double?,

        val profilePath: String?,

        val totalEpisodeCount: Int?
    ) {

        data class Job(
            val creditId: String?,

            val episodeCount: Int?,

            val job: String?
        )
    }
}