package com.application.moviesapp.data.repository

import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.api.response.MovieGenreResponse
import javax.inject.Inject

interface MoviesRepository {
    suspend fun getPopularMoviesList(): String

    suspend fun getMoviesGenreList(): MovieGenreResponse
}

class MoviesRepositoryImpl @Inject constructor(private val movies: MoviesApi): MoviesRepository {
    override suspend fun getPopularMoviesList(): String = movies.getPopularMoviesList()
    override suspend fun getMoviesGenreList(): MovieGenreResponse = movies.getMoviesGenreList()
}
