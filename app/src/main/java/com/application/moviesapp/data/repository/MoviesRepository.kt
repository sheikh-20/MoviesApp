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
import com.application.moviesapp.data.remote.MovieFavouriteDto
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.api.response.MovieNowPlayingDto
import com.application.moviesapp.data.api.response.MovieSearchDto
import com.application.moviesapp.data.api.response.MovieStateDto
import com.application.moviesapp.data.api.response.MovieTopRatedResponse
import com.application.moviesapp.data.api.response.MovieTrailerDto
import com.application.moviesapp.data.api.response.MovieUpdateFavouriteDto
import com.application.moviesapp.data.api.response.TvSeriesDetailsCastDto
import com.application.moviesapp.data.api.response.TvSeriesDetailsDto
import com.application.moviesapp.data.api.response.TvSeriesNowPlayingDto
import com.application.moviesapp.data.api.response.TvSeriesTrailerDto
import com.application.moviesapp.data.local.MoviesDatabase
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.data.remote.MovieFavouritePagingSource
import com.application.moviesapp.data.remote.MovieNewReleasesDto
import com.application.moviesapp.data.remote.MovieNowPlayingPagingSource
import com.application.moviesapp.data.remote.MovieSearchPagingSource
import com.application.moviesapp.data.remote.MovieUpcomingDto
import com.application.moviesapp.data.remote.MoviesDiscoverDto
import com.application.moviesapp.data.remote.MoviesDiscoverPagingSource
import com.application.moviesapp.data.remote.TvSeriesNowPlayingPagingSource
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

interface MoviesRepository {
    suspend fun getDiscoverMoviesList(page: Int = 1): Response<MoviesDiscoverDto>

    suspend fun getMoviesGenreList(): MovieGenreResponse

    suspend fun  getNewReleasesList(): MovieNewReleasesDto

    suspend fun getMoviesTopRated(): MovieTopRatedResponse

    suspend fun getMovieTrending(): MovieSimpleResponse

    suspend fun getCountries(): List<CountryResponse>

    suspend fun getSearchResults(query: String): MovieSimpleResponse

    fun getDiscoverMoviesPagingFlow(genre: String = "", sortBy: String = "", includeAdult: Boolean = false): Flow<PagingData<MoviesDiscoverDto.Result>>

    fun getMoviesNowPlayingPagingFlow(): Flow<PagingData<MovieNowPlayingDto.Result>>

    fun getMovieBySearchPagingFlow(search: String = ""): Flow<PagingData<MovieSearchDto.Result>>

    fun getFavouriteMoviesPagingFlow(): Flow<PagingData<MovieFavouriteDto.Result>>

    suspend fun getMoviesUpcoming(): MovieUpcomingDto

    fun getTvSeriesNowPlayingPagingFlow(): Flow<PagingData<TvSeriesNowPlayingDto.Result>>

    suspend fun getMoviesDetailById(movieId: Int): Response<MovieDetailsDto>

    suspend fun getTvSeriesDetailById(tvSeriesId: Int): Response<TvSeriesDetailsDto>

    suspend fun getMovieDetailsCast(movieId: Int): Response<MovieDetailsCastDto>

    suspend fun getTvSeriesDetailsCast(seriesId: Int): Response<TvSeriesDetailsCastDto>

    suspend fun getMovieTrailer(movieId: Int): Response<MovieTrailerDto>

    suspend fun getTvSeriesTrailer(seriesId: Int): Response<TvSeriesTrailerDto>

    suspend fun updateMovieFavourite(body: RequestBody): Response<MovieUpdateFavouriteDto>

    suspend fun getMovieState(movieId: Int): Response<MovieStateDto>

    suspend fun getMovieGenres(): Response<MovieGenreResponse>

    suspend fun getTvSeriesGenres(): Response<MovieGenreResponse>

    fun readMovieDownload(): Flow<List<MovieDownloadEntity>>

    suspend fun insertMovieDownload(download: MovieDownloadEntity)

    suspend fun deleteMovieDownload(download: MovieDownloadEntity)

    suspend fun getMovieNowPlayingList(page: Int = 1): Response<MovieNowPlayingDto>

    suspend fun getTvSeriesNowPlayingList(): Response<TvSeriesNowPlayingDto>
}

@OptIn(ExperimentalPagingApi::class)
class MoviesRepositoryImpl @Inject constructor(private val movies: MoviesApi,
                                               private val database: MoviesDatabase): MoviesRepository {

    companion object {
        const val PAGE_SIZE = 20
    }


    override suspend fun getDiscoverMoviesList(page: Int): Response<MoviesDiscoverDto> = movies.getDiscoverMoviesList(page = page)
    override suspend fun getMoviesGenreList(): MovieGenreResponse = movies.getMoviesGenreList()
    override suspend fun getNewReleasesList(): MovieNewReleasesDto= movies.getNewReleasesList()
    override suspend fun getMoviesTopRated(): MovieTopRatedResponse = movies.getMovieTopRated()
    override suspend fun getMovieTrending(): MovieSimpleResponse = movies.getMovieTrending()
    override suspend fun getCountries(): List<CountryResponse> = movies.getCountries()
    override suspend fun getSearchResults(query: String): MovieSimpleResponse = movies.getSearch(query)

    override fun getDiscoverMoviesPagingFlow(genre: String, sortBy: String, includeAdult: Boolean): Flow<PagingData<MoviesDiscoverDto.Result>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            MoviesDiscoverPagingSource(moviesApi = movies, genre = genre, sortBy = sortBy, includeAdult = includeAdult)
        }
    ).flow

    override fun getMoviesNowPlayingPagingFlow(): Flow<PagingData<MovieNowPlayingDto.Result>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = 10, initialLoadSize = PAGE_SIZE),
        pagingSourceFactory = {
            MovieNowPlayingPagingSource(movies)
        }
    ).flow

    override fun getMovieBySearchPagingFlow(search: String): Flow<PagingData<MovieSearchDto.Result>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = 10, initialLoadSize = PAGE_SIZE),
        pagingSourceFactory = {
            MovieSearchPagingSource(movies, search)
        }
    ).flow

    override suspend fun getMoviesUpcoming(): MovieUpcomingDto = movies.getMovieUpcomingList()

    override fun getTvSeriesNowPlayingPagingFlow(): Flow<PagingData<TvSeriesNowPlayingDto.Result>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            TvSeriesNowPlayingPagingSource(movies)
        }
    ).flow

    override fun getFavouriteMoviesPagingFlow(): Flow<PagingData<MovieFavouriteDto.Result>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            MovieFavouritePagingSource(movies)
        }
    ).flow

    override suspend fun getMoviesDetailById(movieId: Int): Response<MovieDetailsDto> = movies.getMovieDetailsById(movieId)

    override suspend fun getTvSeriesDetailById(tvSeriesId: Int): Response<TvSeriesDetailsDto> = movies.getTvSeriesDetailsId(tvSeriesId)

    override suspend fun getMovieDetailsCast(movieId: Int): Response<MovieDetailsCastDto> = movies.getMovieDetailsCast(movieId)

    override suspend fun getTvSeriesDetailsCast(seriesId: Int): Response<TvSeriesDetailsCastDto> = movies.getTvSeriesDetailsCast(seriesId)

    override suspend fun getMovieTrailer(movieId: Int): Response<MovieTrailerDto> = movies.getMovieTrailer(movieId)

    override suspend fun getTvSeriesTrailer(seriesId: Int): Response<TvSeriesTrailerDto> = movies.getTvSeriesTrailer(seriesId)

    override suspend fun updateMovieFavourite(body: RequestBody): Response<MovieUpdateFavouriteDto> = movies.updateMovieFavourite(body = body)

    override suspend fun getMovieState(movieId: Int): Response<MovieStateDto> = movies.getMovieState(movieId)

    override suspend fun getMovieGenres(): Response<MovieGenreResponse> = movies.getMovieGenres()

    override suspend fun getTvSeriesGenres(): Response<MovieGenreResponse> = movies.getTVSeriesGenres()

    override fun readMovieDownload(): Flow<List<MovieDownloadEntity>> = database.movieDownloadDao.getAllDownloads()

    override suspend fun insertMovieDownload(download: MovieDownloadEntity) =  database.movieDownloadDao.insertDownload(download)

    override suspend fun deleteMovieDownload(download: MovieDownloadEntity) = database.movieDownloadDao.deleteDownload(download)

    override suspend fun getMovieNowPlayingList(page: Int): Response<MovieNowPlayingDto> = movies.getNowPlayingMovieList(page = page)

    override suspend fun getTvSeriesNowPlayingList(): Response<TvSeriesNowPlayingDto> = movies.getNowPlayingSeriesList()
}
