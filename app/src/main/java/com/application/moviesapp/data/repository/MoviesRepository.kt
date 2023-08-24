package com.application.moviesapp.data.repository

import com.application.moviesapp.data.api.response.MovieSimpleResponse
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.api.response.CountryResponse
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.api.response.MovieNewReleasesResponse
import com.application.moviesapp.data.api.response.MovieTopRatedResponse
import com.application.moviesapp.data.remote.MoviesDto
import javax.inject.Inject

interface MoviesRepository {
    suspend fun getPopularMoviesList(): MoviesDto

    suspend fun getMoviesGenreList(): MovieGenreResponse

    suspend fun  getNewReleasesList(): MovieNewReleasesResponse

    suspend fun getMoviesTopRated(): MovieTopRatedResponse

    suspend fun getMovieTrending(): MovieSimpleResponse

    suspend fun getCountries(): List<CountryResponse>

    suspend fun getSearchResults(query: String): MovieSimpleResponse
}

class MoviesRepositoryImpl @Inject constructor(private val movies: MoviesApi): MoviesRepository {
    override suspend fun getPopularMoviesList(): MoviesDto = movies.getPopularMoviesList()
    override suspend fun getMoviesGenreList(): MovieGenreResponse = movies.getMoviesGenreList()
    override suspend fun getNewReleasesList(): MovieNewReleasesResponse = movies. getNewReleasesList()
    override suspend fun getMoviesTopRated(): MovieTopRatedResponse = movies.getMovieTopRated()
    override suspend fun getMovieTrending(): MovieSimpleResponse = movies.getMovieTrending()
    override suspend fun getCountries(): List<CountryResponse> = movies.getCountries()
    override suspend fun getSearchResults(query: String): MovieSimpleResponse = movies.getSearch(query)
}
