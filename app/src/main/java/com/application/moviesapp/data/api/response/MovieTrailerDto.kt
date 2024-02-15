package com.application.moviesapp.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieTrailerDto(
    @SerialName("id")
    val id: Int?,

    @SerialName("results")
    val results: List<Result?>?
) {

    @Serializable
    data class Result(
        @SerialName("id")
        val id: String?,

        @SerialName("iso_3166_1")
        val isoOne: String?,

        @SerialName("iso_639_1")
        val isoTwo: String?,

        @SerialName("key")
        val key: String?,

        @SerialName("name")
        val name: String?,

        @SerialName("official")
        val official: Boolean?,

        @SerialName("published_at")
        val publishedAt: String?,

        @SerialName("site")
        val site: String?,

        @SerialName("size")
        val size: Int?,

        @SerialName("type")
        val type: String?
    )
}