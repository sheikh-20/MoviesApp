package com.application.moviesapp.domain

import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.api.response.MovieNewReleasesResponse
import com.application.moviesapp.data.api.response.MovieTopRatedResponse
import com.application.moviesapp.data.remote.MovieNewReleasesDto

data class MoviesWithNewReleases(val topRatedResponse: MovieTopRatedResponse,
                                 val genreResponse: MovieGenreResponse,
                                 val newReleasesResponse: MovieNewReleasesDto) {

    private val genreList = mutableSetOf<MovieGenreResponse.Genre?>()
    val titleGenre get() = genreList.map { it?.name }.joinToString(", ")

    init {
        topRatedResponse.results?.first()?.genreIds?.forEach {  genreId ->
            genreResponse.genres?.forEach { genre ->
                if (genreId?.equals(genre?.id) == true) {
                    genreList.add(genre)
                }
            }
        }
    }
}
