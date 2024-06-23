package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.api.response.TvSeriesReviewDto
import com.application.moviesapp.domain.model.MovieReview
import com.application.moviesapp.domain.model.TvSeriesReview

fun TvSeriesReviewDto.Result.toDomain(): TvSeriesReview {
    return TvSeriesReview(
        author = this.author,
        content = this.content,
        createdAt = this.createdAt,
        id = this.id,
        updatedAt = this.updatedAt,
        url = this.url,
        authorDetails = TvSeriesReview.AuthorDetails(
            avatarPath = this.authorDetails?.avatarPath,
            name = this.authorDetails?.name,
            username = this.authorDetails?.username,
            rating = this.authorDetails?.rating
        )
    )
}