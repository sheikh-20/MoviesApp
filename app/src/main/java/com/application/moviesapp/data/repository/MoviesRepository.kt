package com.application.moviesapp.data.repository

import com.application.moviesapp.data.api.MoviesApi
import javax.inject.Inject

interface MoviesRepository {
    suspend fun getPopularMoviesList(): String
}

class MoviesRepositoryImpl @Inject constructor(private val movies: MoviesApi): MoviesRepository {
    override suspend fun getPopularMoviesList(): String = movies.getPopularMoviesList()
}
