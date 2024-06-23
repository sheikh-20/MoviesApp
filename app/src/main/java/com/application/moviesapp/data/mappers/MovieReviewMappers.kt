package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.api.response.MovieReviewDto
import com.application.moviesapp.domain.model.MovieReview

fun MovieReviewDto.Result.toDomain(): MovieReview {
    return MovieReview(
        author = this.author,
        content = this.content,
        createdAt = this.createdAt,
        id = this.id,
        updatedAt = this.updatedAt,
        url = this.url,
        authorDetails = MovieReview.AuthorDetails(
            avatarPath = this.authorDetails?.avatarPath,
            name = this.authorDetails?.name,
            username = this.authorDetails?.username,
            rating = this.authorDetails?.rating
        )
    )
}