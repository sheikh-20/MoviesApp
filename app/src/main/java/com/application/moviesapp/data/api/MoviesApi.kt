package com.application.moviesapp.data.api

import com.application.moviesapp.data.api.response.MovieGenreResponse
import retrofit2.http.GET

interface MoviesApi {
    @GET("/3/movie/popular")
    suspend fun getPopularMoviesList(): String

    @GET("3/genre/movie/list")
    suspend fun getMoviesGenreList(): MovieGenreResponse
}