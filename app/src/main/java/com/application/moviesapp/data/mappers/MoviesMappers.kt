package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.api.response.MovieDetailsDto
import com.application.moviesapp.data.local.entity.MovieNewReleaseEntity
import com.application.moviesapp.data.local.entity.MovieUpcomingEntity
import com.application.moviesapp.data.local.entity.MoviesEntity
import com.application.moviesapp.data.remote.MovieNewReleasesDto
import com.application.moviesapp.data.remote.MovieUpcomingDto
import com.application.moviesapp.data.remote.MoviesDto
import com.application.moviesapp.domain.Movies
import com.application.moviesapp.domain.model.MovieNewRelease
import com.application.moviesapp.domain.model.MovieUpcoming
import com.application.moviesapp.domain.model.MoviesDetail

fun MoviesDto.Result.toMoviesEntity(): MoviesEntity {
    return MoviesEntity(
        adult = adult,
        backdropPath = backdropPath,
        movieId = id,
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



fun MovieNewReleasesDto.Result.toMoviesEntity(): MovieNewReleaseEntity {
    return MovieNewReleaseEntity(
        adult = adult,
        backdropPath = backdropPath,
        movieId = id,
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

fun MovieNewReleaseEntity.toMovies(): MovieNewRelease {
    return MovieNewRelease(
        adult = adult,
        backdropPath = backdropPath,
        id = id,
        movieId = movieId,
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


fun MovieUpcomingDto.Result.toMoviesEntity(): MovieUpcomingEntity {
    return MovieUpcomingEntity(
        adult = adult,
        backdropPath = backdropPath,
        movieId = id,
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

fun MovieUpcomingEntity.toMovies(): MovieUpcoming {
    return MovieUpcoming(
        adult = adult,
        backdropPath = backdropPath,
        id = id,
        movieId = movieId,
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