package com.application.moviesapp.data.api

import retrofit2.http.GET

interface MoviesApi {
    @GET("/3/movie/popular")
    suspend fun getPopularMoviesList(): String
}