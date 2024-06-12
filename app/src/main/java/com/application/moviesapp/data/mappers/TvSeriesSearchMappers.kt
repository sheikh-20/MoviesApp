package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.api.response.TvSeriesSearchDto
import com.application.moviesapp.domain.model.TvSeriesSearch

fun TvSeriesSearchDto.Result.toTvSeries(): TvSeriesSearch {
    return TvSeriesSearch(
        adult = adult,
        backdropPath = backdropPath,
        firstAirDate = firstAirDate,
        genreIds = genreIds,
        id = id,
        name = name,
        originCountry = originCountry,
        originalLanguage = originalLanguage,
        originalName = originalName,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}