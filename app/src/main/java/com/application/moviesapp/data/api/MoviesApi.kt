package com.application.moviesapp.data.api

import com.application.moviesapp.data.api.response.CountryResponse
import com.application.moviesapp.data.api.response.MovieDetailsCastDto
import com.application.moviesapp.data.api.response.MovieDetailsDto
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.api.response.MovieSimpleResponse
import com.application.moviesapp.data.api.response.MovieTopRatedResponse
import com.application.moviesapp.data.remote.MovieNewReleasesDto
import com.application.moviesapp.data.remote.MovieUpcomingDto
import com.application.moviesapp.data.remote.MoviesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("/3/movie/popular")
    suspend fun getPopularMoviesList(@Query("language") language: String = "en-US", @Query("page") page: Int = 1): MoviesDto

    @GET("/3/genre/movie/list")
    suspend fun getMoviesGenreList(): MovieGenreResponse

    @GET("/3/movie/now_playing")
    suspend fun getNewReleasesList(@Query("language") language: String = "en-US", @Query("page") page: Int = 1): MovieNewReleasesDto

    @GET("/3/movie/upcoming")
    suspend fun getMovieUpcomingList(@Query("language") language: String = "en-US", @Query("page") page: Int = 1): MovieUpcomingDto

    @GET("/3/movie/top_rated")
    suspend fun getMovieTopRated(): MovieTopRatedResponse

    @GET("/3/trending/movie/{time_window}")
    suspend fun getMovieTrending(@Path("time_window") timeWindow: String = "day"): MovieSimpleResponse

    @GET("3/configuration/countries")
    suspend fun getCountries(): List<CountryResponse>

    @GET("/3/search/movie")
    suspend fun getSearch(@Query("query") query: String, @Query("include_adult") includeAdult: Boolean = false, @Query("language") language: String = "en-US", @Query("page") page: Int = 1): MovieSimpleResponse

    @GET("/3/movie/{movie_id}")
    suspend fun getMovieDetailsById(@Path("movie_id") movieId: Int, @Query("language") language: String = "en-US"): Response<MovieDetailsDto>

    @GET("/3/movie/{movie_id}/credits")
    suspend fun getMovieDetailsCast(@Path("movie_id") movieId: Int): Response<MovieDetailsCastDto>
}