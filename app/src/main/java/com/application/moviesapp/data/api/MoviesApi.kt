package com.application.moviesapp.data.api

import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.api.response.MovieNewReleasesResponse
import com.application.moviesapp.data.api.response.MovieTopRatedResponse
import retrofit2.http.GET

interface MoviesApi {
    @GET("/3/movie/popular")
    suspend fun getPopularMoviesList(): String

    @GET("/3/genre/movie/list")
    suspend fun getMoviesGenreList(): MovieGenreResponse

    @GET("/3/movie/now_playing")
    suspend fun getNewReleasesList(): MovieNewReleasesResponse

    @GET("/3/movie/top_rated")
    suspend fun getMovieTopRated(): MovieTopRatedResponse
}