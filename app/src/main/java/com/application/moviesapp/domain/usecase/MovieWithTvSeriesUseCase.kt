package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.mappers.toMovieGenre
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieWithTvSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface MovieWithTvSeriesUseCase {
    suspend operator fun invoke(): Resource<MovieWithTvSeries>
}

class GetMovieWithTvSeriesInteractor @Inject constructor(private val moviesRepository: MoviesRepository): MovieWithTvSeriesUseCase {

    private companion object {
        const val TAG = "GetMovieWithTvSeriesInteractor"
    }

    override suspend operator fun invoke(): Resource<MovieWithTvSeries> {
        return try {
            Resource.Loading

            withContext(Dispatchers.Default) {
                val movies = async { moviesRepository.getMovieNowPlayingList() }.await()
                val tvSeries = async { moviesRepository.getTvSeriesNowPlayingList() }.await()
                val genres = async { moviesRepository.getMovieGenres() }.await()

                if (movies.isSuccessful && tvSeries.isSuccessful && genres.isSuccessful) {
                    Resource.Success(
                        MovieWithTvSeries(
                            movies = movies.body()?.results?.map { it?.toMovies() },
                            tvSeries = tvSeries.body()?.results?.map { it?.toMovies() },
                            genres = genres.body()?.toMovieGenre()
                        )
                    )
                } else if (movies.code() == 401 && tvSeries.code() == 401 && genres.code() == 401) {
                    Timber.tag(TAG).e("${movies.errorBody().toString()}\n${tvSeries.errorBody().toString()}\n${genres.errorBody().toString()}")
                    Resource.Failure(Throwable())
                } else if (movies.code() == 404 && tvSeries.code() == 404 && genres.code() == 404) {
                    Timber.tag(TAG).e("${movies.errorBody().toString()}\n${tvSeries.errorBody().toString()}\n${genres.errorBody().toString()}")
                    Resource.Failure(Throwable())
                } else {
                    Timber.tag(TAG).e("${movies.errorBody().toString()}\n${tvSeries.errorBody().toString()}\n${genres.errorBody().toString()}")
                    Resource.Failure(Throwable())
                }
            }

        } catch (throwable: Throwable) {
            Timber.tag(TAG).e(throwable)
            Resource.Failure(throwable)
        }
    }
}