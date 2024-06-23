package com.application.moviesapp.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvSeriesReviewDto(

    @SerialName("id")
    val id: Int?,

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
    data class Result(

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
}