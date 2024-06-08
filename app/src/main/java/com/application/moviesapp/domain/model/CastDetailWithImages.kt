package com.application.moviesapp.domain.model

import com.application.moviesapp.data.api.response.CastMovieCreditsDto

data class CastDetailWithImages(
    val detail: CastDetail,
    val images: CastImages,
    val castMovieCredits: CastMovieCredits,
    val castTvSeriesCredits: CastTvSeriesCredit
)