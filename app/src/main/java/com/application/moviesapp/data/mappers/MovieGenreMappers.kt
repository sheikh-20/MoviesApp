package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.domain.model.MovieGenre

fun MovieGenreResponse.toMovieGenre(): MovieGenre {
    return MovieGenre(
        genres = genres?.map {
            MovieGenre.Genre(id = it?.id, name = it?.name)
        }
    )
}