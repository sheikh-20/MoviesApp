package com.application.moviesapp.data.api

import com.application.moviesapp.data.SORT_BY
import com.application.moviesapp.data.api.response.CountryResponse
import com.application.moviesapp.data.api.response.MovieDetailsCastDto
import com.application.moviesapp.data.api.response.MovieDetailsDto
import com.application.moviesapp.data.remote.MovieFavouriteDto
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.api.response.MovieNowPlayingDto
import com.application.moviesapp.data.api.response.MovieSearchDto
import com.application.moviesapp.data.api.response.MovieSimpleResponse
import com.application.moviesapp.data.api.response.MovieStateDto
import com.application.moviesapp.data.api.response.MovieTopRatedResponse
import com.application.moviesapp.data.api.response.MovieTrailerDto
import com.application.moviesapp.data.api.response.MovieUpdateFavouriteDto
import com.application.moviesapp.data.api.response.TvSeriesDetailsCastDto
import com.application.moviesapp.data.api.response.TvSeriesDetailsDto
import com.application.moviesapp.data.api.response.TvSeriesNowPlayingDto
import com.application.moviesapp.data.api.response.TvSeriesTrailerDto
import com.application.moviesapp.data.remote.MovieNewReleasesDto
import com.application.moviesapp.data.remote.MovieUpcomingDto
import com.application.moviesapp.data.remote.MoviesDiscoverDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("/3/discover/movie")
    suspend fun getDiscoverMoviesList(@Query("language") language: String = "en-US", @Query("page") page: Int = 1, @Query("with_genres") genres: String = "", @Query("sort_by") sortBy: String = SORT_BY.POPULARITY.title, @Query("include_adult") includeAdult: Boolean = false): Response<MoviesDiscoverDto>

    @GET("/3/genre/movie/list")
    suspend fun getMoviesGenreList(): MovieGenreResponse

    @GET("/3/genre/movie/list")
    suspend fun getMovieGenres(): Response<MovieGenreResponse>

    @GET("/3/genre/tv/list")
    suspend fun getTVSeriesGenres(): Response<MovieGenreResponse>

    @GET("/3/movie/now_playing")
    suspend fun getNewReleasesList(@Query("language") language: String = "en-US", @Query("page") page: Int = 1): MovieNewReleasesDto

    @GET("/3/movie/now_playing")
    suspend fun getNowPlayingMovieList(@Query("language") language: String = "en-US", @Query("page") page: Int = 1): Response<MovieNowPlayingDto>

    @GET("/3/tv/airing_today")
    suspend fun getNowPlayingSeriesList(@Query("language") language: String = "en-US", @Query("page") page: Int = 1): Response<TvSeriesNowPlayingDto>

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

    @GET("/3/tv/{series_id}")
    suspend fun getTvSeriesDetailsId(@Path("series_id") seriesId: Int, @Query("language") language: String = "en-US"): Response<TvSeriesDetailsDto>

    @GET("/3/movie/{movie_id}/credits")
    suspend fun getMovieDetailsCast(@Path("movie_id") movieId: Int): Response<MovieDetailsCastDto>

    @GET("/3/tv/{series_id}/aggregate_credits")
    suspend fun getTvSeriesDetailsCast(@Path("series_id") seriesId: Int, @Query("language") language: String = "en-US"): Response<TvSeriesDetailsCastDto>

    @GET("/3/movie/{movie_id}/videos")
    suspend fun getMovieTrailer(@Path("movie_id") movieId: Int): Response<MovieTrailerDto>

    @GET("/3/tv/{series_id}/videos")
    suspend fun getTvSeriesTrailer(@Path("series_id") seriesId: Int): Response<TvSeriesTrailerDto>

    @GET("/3/account/{account_id}/favorite/movies")
    suspend fun getMovieFavourite(@Path("account_id") accountId: Int = 20210857, @Query("language") language: String = "en-US", @Query("page") page: Int = 1): Response<MovieFavouriteDto>

    @POST("/3/account/{account_id}/favorite")
    suspend fun updateMovieFavourite(@Path("account_id") accountId: Int = 20210857, @Body body: RequestBody): Response<MovieUpdateFavouriteDto>

    @GET("/3/movie/{movie_id}/account_states")
    suspend fun getMovieState(@Path("movie_id") movieId: Int): Response<MovieStateDto>

    @GET("/3/search/movie")
    suspend fun getMovieBySearch(@Query("language") language: String = "en-US", @Query("query") query: String = "", @Query("page") page: Int = 1): Response<MovieSearchDto>
}