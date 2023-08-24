package com.application.moviesapp.data.api

import com.application.moviesapp.data.api.response.CountryResponse
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.api.response.MovieNewReleasesResponse
import com.application.moviesapp.data.api.response.MovieSimpleResponse
import com.application.moviesapp.data.api.response.MovieTopRatedResponse
import com.application.moviesapp.data.remote.MoviesDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("/3/movie/popular")
    suspend fun getPopularMoviesList(@Query("language") language: String = "en-US", @Query("page") page: Int = 1): MoviesDto

    @GET("/3/genre/movie/list")
    suspend fun getMoviesGenreList(): MovieGenreResponse

    @GET("/3/movie/now_playing")
    suspend fun getNewReleasesList(): MovieNewReleasesResponse

    @GET("/3/movie/top_rated")
    suspend fun getMovieTopRated(): MovieTopRatedResponse

    @GET("/3/trending/movie/{time_window}")
    suspend fun getMovieTrending(@Path("time_window") timeWindow: String = "day"): MovieSimpleResponse

    @GET("3/configuration/countries")
    suspend fun getCountries(): List<CountryResponse>

    @GET("/3/search/movie")
    suspend fun getSearch(@Query("query") query: String, @Query("include_adult") includeAdult: Boolean = false, @Query("language") language: String = "en-US", @Query("page") page: Int = 1): MovieSimpleResponse
}