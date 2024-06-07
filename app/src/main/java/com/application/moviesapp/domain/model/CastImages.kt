package com.application.moviesapp.domain.model

data class CastImages(
    val id: Int?,
    val profiles: List<Profile?>?
) {
    data class Profile(
        val aspectRatio: Double?,
        val filePath: String?,
        val height: Int?,
        val iso: String?,
        val voteAverage: Double?,
        val voteCount: Int?,
        val width: Int?
    )
}