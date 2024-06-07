package com.application.moviesapp.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CastImagesDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("profiles")
    val profiles: List<Profile?>?
) {

    @Serializable
    data class Profile(
        @SerialName("aspect_ratio")
        val aspectRatio: Double?,

        @SerialName("file_path")
        val filePath: String?,

        @SerialName("height")
        val height: Int?,

        @SerialName("iso_639_1")
        val iso: String?,

        @SerialName("vote_average")
        val voteAverage: Double?,

        @SerialName("vote_count")
        val voteCount: Int?,

        @SerialName("width")
        val width: Int?
    )
}