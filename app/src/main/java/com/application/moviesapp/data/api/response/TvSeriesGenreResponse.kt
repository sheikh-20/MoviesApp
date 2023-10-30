package com.application.moviesapp.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvSeriesGenreResponse(
    @SerialName("genres")
    val genres: List<Genre?>?
) {
    @Serializable
    data class Genre(
        @SerialName("id")
        val id: Int?,

        @SerialName("name")
        val name: String?
    )
}