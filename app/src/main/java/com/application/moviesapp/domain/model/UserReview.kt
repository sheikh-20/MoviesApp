package com.application.moviesapp.domain.model

data class UserReview(
    val author: String?,
    val authorDetails: AuthorDetails?,
    val content: String?,
    val createdAt: String?,
    val id: String?,
    val updatedAt: String?,
    val url: String?
) {
    data class AuthorDetails(
        val avatarPath: String?,
        val name: String?,
        val rating: Double?,
        val username: String?
    )
}