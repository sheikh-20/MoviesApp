package com.application.moviesapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.application.moviesapp.data.api.response.MovieSimpleResponse
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.api.response.CountryResponse
import com.application.moviesapp.data.api.response.MovieDetailsCastDto
import com.application.moviesapp.data.api.response.MovieDetailsDto
import com.application.moviesapp.data.api.response.MovieFavouriteDto
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.api.response.MovieStateDto
import com.application.moviesapp.data.api.response.MovieTopRatedResponse
import com.application.moviesapp.data.api.response.MovieTrailerDto
import com.application.moviesapp.data.api.response.MovieUpdateFavouriteDto
import com.application.moviesapp.data.local.MoviesDatabase
import com.application.moviesapp.data.local.entity.MovieNewReleaseEntity
import com.application.moviesapp.data.local.entity.MovieUpcomingEntity
import com.application.moviesapp.data.local.entity.MoviesEntity
import com.application.moviesapp.data.remote.MovieNewReleasesDto
import com.application.moviesapp.data.remote.MovieUpcomingDto
import com.application.moviesapp.data.remote.MoviesDto
import com.application.moviesapp.data.remote.MoviesNewReleaseRemoteMediator
import com.application.moviesapp.data.remote.MoviesRemoteMediator
import com.application.moviesapp.data.remote.MoviesUpcomingRemoteMediator
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

interface MoviesRepository {
    suspend fun getPopularMoviesList(): MoviesDto

    suspend fun getMoviesGenreList(): MovieGenreResponse

    suspend fun  getNewReleasesList(): MovieNewReleasesDto

    suspend fun getMoviesTopRated(): MovieTopRatedResponse

    suspend fun getMovieTrending(): MovieSimpleResponse

    suspend fun getCountries(): List<CountryResponse>

    suspend fun getSearchResults(query: String): MovieSimpleResponse

    fun getMoviesPagingFlow(): Flow<PagingData<MoviesEntity>>

    fun getMoviesNewReleasePagingFlow(): Flow<PagingData<MovieNewReleaseEntity>>

    suspend fun getMoviesUpcoming(): MovieUpcomingDto

    fun getMoviesUpcomingPagingFlow(): Flow<PagingData<MovieUpcomingEntity>>

    suspend fun getMoviesDetailById(movieId: Int): Response<MovieDetailsDto>

    suspend fun getMovieDetailsCast(movieId: Int): Response<MovieDetailsCastDto>

    suspend fun getMovieTrailer(movieId: Int): Response<MovieTrailerDto>

    suspend fun getMovieFavourite(): Response<MovieFavouriteDto>

    suspend fun updateMovieFavourite(body: RequestBody): Response<MovieUpdateFavouriteDto>

    suspend fun getMovieState(movieId: Int): Response<MovieStateDto>
}

@OptIn(ExperimentalPagingApi::class)
class MoviesRepositoryImpl @Inject constructor(private val movies: MoviesApi, private val database: MoviesDatabase): MoviesRepository {
    override suspend fun getPopularMoviesList(): MoviesDto = movies.getPopularMoviesList()
    override suspend fun getMoviesGenreList(): MovieGenreResponse = movies.getMoviesGenreList()
    override suspend fun getNewReleasesList(): MovieNewReleasesDto= movies.getNewReleasesList()
    override suspend fun getMoviesTopRated(): MovieTopRatedResponse = movies.getMovieTopRated()
    override suspend fun getMovieTrending(): MovieSimpleResponse = movies.getMovieTrending()
    override suspend fun getCountries(): List<CountryResponse> = movies.getCountries()
    override suspend fun getSearchResults(query: String): MovieSimpleResponse = movies.getSearch(query)

    override fun getMoviesPagingFlow(): Flow<PagingData<MoviesEntity>> = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = MoviesRemoteMediator(movies, database),
        pagingSourceFactory = {
            database.moviesDao.pagingSource()
        }
    ).flow

    override fun getMoviesNewReleasePagingFlow(): Flow<PagingData<MovieNewReleaseEntity>> = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = MoviesNewReleaseRemoteMediator(movies, database),
        pagingSourceFactory = {
            database.movieNewReleaseDao.pagingSource()
        }
    ).flow

    override suspend fun getMoviesUpcoming(): MovieUpcomingDto = movies.getMovieUpcomingList()

    override fun getMoviesUpcomingPagingFlow(): Flow<PagingData<MovieUpcomingEntity>> = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = MoviesUpcomingRemoteMediator(movies, database),
        pagingSourceFactory = {
            database.moviesUpcomingDao.pagingSource()
        }
    ).flow

    override suspend fun getMoviesDetailById(movieId: Int): Response<MovieDetailsDto> = movies.getMovieDetailsById(movieId)

    override suspend fun getMovieDetailsCast(movieId: Int): Response<MovieDetailsCastDto> = movies.getMovieDetailsCast(movieId)

    override suspend fun getMovieTrailer(movieId: Int): Response<MovieTrailerDto> = movies.getMovieTrailer(movieId)

    override suspend fun getMovieFavourite(): Response<MovieFavouriteDto> = movies.getMovieFavourite()

    override suspend fun updateMovieFavourite(body: RequestBody): Response<MovieUpdateFavouriteDto> = movies.updateMovieFavourite(body = body)

    override suspend fun getMovieState(movieId: Int): Response<MovieStateDto> = movies.getMovieState(movieId)
}
