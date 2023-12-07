package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.mappers.toMovieGenre
import com.application.moviesapp.data.mappers.toTvSeries
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieGenre
import com.application.moviesapp.domain.model.TvSeriesEpisodes
import timber.log.Timber

interface TvSeriesEpisodesUseCase {
    suspend operator fun invoke(seriesId: Int, seasonNumber: Int): Resource<TvSeriesEpisodes>
}

class GetTvSeriesEpisodesUseCase(private val repository: MoviesRepository): TvSeriesEpisodesUseCase {

    private companion object {
        const val TAG = "GetTvSeriesEpisodesUseCase"
    }

    override suspend fun invoke(seriesId: Int, seasonNumber: Int): Resource<TvSeriesEpisodes> {
        return try {
            Resource.Loading

            val result = repository.getTvSeriesEpisodes(seriesId, seasonNumber)

            if (result.isSuccessful) {
                val episodes = result.body()?.toTvSeries()  ?: TvSeriesEpisodes(null)
                Timber.tag(TAG).d(episodes.toString())
                Resource.Success(data = episodes)
            } else if (result.code() == 400 || result.code() == 401 || result.code() == 404) {
                Timber.tag(TAG).e("404 error")
                Resource.Failure(Throwable())
            } else {
                Timber.tag(TAG).e("Server error")
                Resource.Failure(Throwable())
            }
        } catch (throwable: Throwable) {
            Timber.tag(TAG).e(throwable)
            Resource.Failure(throwable)
        }
    }
}