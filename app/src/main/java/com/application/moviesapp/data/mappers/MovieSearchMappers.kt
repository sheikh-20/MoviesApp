package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.api.response.MovieSearchDto
import com.application.moviesapp.domain.model.MovieSearch

fun MovieSearchDto.Result.toMovie(): MovieSearch {
    return MovieSearch(
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}