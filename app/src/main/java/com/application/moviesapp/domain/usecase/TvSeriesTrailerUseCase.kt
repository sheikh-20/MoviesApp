package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.mappers.toTvSeries
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.data.repository.YoutubeRepository
import com.application.moviesapp.domain.model.MovieTrailer
import com.application.moviesapp.domain.model.MovieTrailerWithYoutube
import com.application.moviesapp.domain.model.TvSeriesTrailer
import com.application.moviesapp.domain.model.TvSeriesTrailerWithYoutube
import timber.log.Timber
import javax.inject.Inject


interface TvSeriesTrailerUseCase {
    suspend operator fun invoke(seriesId: Int): Resource<List<TvSeriesTrailerWithYoutube>>
}

class GetTvSeriesTrailerInteractor @Inject constructor(private val moviesRepository: MoviesRepository,
                                                    private val youtubeRepository: YoutubeRepository
): TvSeriesTrailerUseCase {

    private companion object {
        const val TAG = "GetTvSeriesTrailerInteractor"
    }

    override suspend fun invoke(seriesId: Int): Resource<List<TvSeriesTrailerWithYoutube>> {
        return try {
            val result = moviesRepository.getTvSeriesTrailer(seriesId)

            if (result.isSuccessful) {
                val response = result.body()?.toTvSeries() ?: TvSeriesTrailer(null, null)

                val youtube = response.results?.map {
                    val youtubeResponse = youtubeRepository.videoThumbnail(
                        part = arrayOf(
                            "snippet",
                            "contentDetails"
                        ), id = it?.key ?: ""
                    )

                    val trailer = youtubeResponse.body()?.items?.get(0)?.run {
                        TvSeriesTrailerWithYoutube(
                            id = id,
                            title = snippet?.title,
                            duration = contentDetails?.duration,
                            thumbnail = snippet?.thumbnails?.standard?.url)
                    }

                    trailer ?: TvSeriesTrailerWithYoutube(null, null, null, null)
                }
                Timber.tag(TAG).d(youtube.toString())
                Resource.Success(data = youtube!!)

            } else {
                Timber.tag(TAG).e(result.errorBody().toString())
                Resource.Failure(Throwable())
            }

        } catch (throwable: Throwable) {
            Timber.tag(TAG).e(throwable)
            Resource.Failure(throwable)
        }
    }
}