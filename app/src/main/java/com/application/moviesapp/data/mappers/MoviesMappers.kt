package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.local.entity.MoviesEntity
import com.application.moviesapp.data.remote.MoviesDto
import com.application.moviesapp.domain.Movies

fun MoviesDto.Result.toMoviesEntity(): MoviesEntity {
    return MoviesEntity(
        adult = adult,
        backdropPath = backdropPath,
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
        voteCount = voteCount)
}

fun MoviesEntity.toMovies(): Movies {
    return Movies(
        adult = adult,
        backdropPath = backdropPath,
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
