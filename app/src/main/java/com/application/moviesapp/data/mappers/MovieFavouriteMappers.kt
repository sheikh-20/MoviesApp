package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.remote.MovieFavouriteDto
import com.application.moviesapp.domain.model.MovieFavourite

fun MovieFavouriteDto.Result.toMovie(): MovieFavourite {
    return MovieFavourite(
        adult, backdropPath, id, originalLanguage, originalTitle, overview, popularity, posterPath, releaseDate, title, video, voteAverage, voteCount
    )
}