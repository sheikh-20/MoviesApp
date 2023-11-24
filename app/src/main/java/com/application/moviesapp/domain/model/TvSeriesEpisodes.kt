package com.application.moviesapp.domain.model

data class TvSeriesEpisodes(
    val episodes: List<Episode?>?,
) {

    data class Episode(
        val airDate: String?,

        val episodeNumber: Int?,

        val episodeType: String?,

        val guestStars: List<GuestStar?>?,

        val id: Int?,

        val name: String?,

        val overview: String?,

        val productionCode: String?,

        val runtime: Int?,

        val seasonNumber: Int?,

        val showId: Int?,

        val stillPath: String?,

        val voteAverage: Double?,

        val voteCount: Int?
    ) {

        data class GuestStar(

            val adult: Boolean?,

            val character: String?,

            val creditId: String?,

            val gender: Int?,

            val id: Int?,

            val knownForDepartment: String?,

            val name: String?,

            val order: Int?,

            val originalName: String?,

            val popularity: Double?,

            val profilePath: String?
        )
    }
}