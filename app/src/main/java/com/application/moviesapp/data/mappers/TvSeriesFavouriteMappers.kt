package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.remote.TvSeriesFavouriteDto
import com.application.moviesapp.domain.model.TvSeriesFavourite

fun TvSeriesFavouriteDto.Result.toDomain(): TvSeriesFavourite {
    return TvSeriesFavourite(
        adult, backdropPath, firstAirDate, genreIds, id, name, originCountry, originalLanguage, originalName, overview, popularity, posterPath, voteAverage, voteCount
    )
}