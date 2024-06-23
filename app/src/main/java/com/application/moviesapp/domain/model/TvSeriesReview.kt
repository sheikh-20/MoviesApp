package com.application.moviesapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvSeriesReview(

    @SerialName("author")
    val author: String?,

    @SerialName("author_details")
    val authorDetails: AuthorDetails?,

    @SerialName("content")
    val content: String?,

    @SerialName("created_at")
    val createdAt: String?,

    @SerialName("id")
    val id: String?,

    @SerialName("updated_at")
    val updatedAt: String?,

    @SerialName("url")
    val url: String?
) {

    @Serializable
    data class AuthorDetails(

        @SerialName("avatar_path")
        val avatarPath: String?,

        @SerialName("name")
        val name: String?,

        @SerialName("rating")
        val rating: Double?,

        @SerialName("username")
        val username: String?
    )
}