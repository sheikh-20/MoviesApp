package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.api.response.MovieTrailerDto
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.mappers.toYoutubeThumbnail
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.data.repository.YoutubeRepository
import com.application.moviesapp.domain.model.MovieTrailer
import com.application.moviesapp.domain.model.MovieTrailerWithYoutube
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

interface MovieTrailerUseCase {
    suspend operator fun invoke(movieId: Int): Resource<List<MovieTrailerWithYoutube>>
}

class GetMovieTrailerInteractor @Inject constructor(private val moviesRepository: MoviesRepository,
                                                    private val youtubeRepository: YoutubeRepository): MovieTrailerUseCase {

    private companion object {
        const val TAG = "GetMovieTrailerInteractor"
    }

    override suspend fun invoke(movieId: Int): Resource<List<MovieTrailerWithYoutube>> {
        return try {
            val result = moviesRepository.getMovieTrailer(movieId)

            if (result.isSuccessful) {
                val response = result.body()?.toMovies() ?: MovieTrailer(null, null)

                val youtube = response.results?.map {
                    val youtubeResponse = youtubeRepository.videoThumbnail(
                        part = arrayOf(
                            "snippet",
                            "contentDetails"
                        ), id = it?.key ?: ""
                    )

                    val trailer = youtubeResponse.body()?.items?.get(0)?.run {
                        MovieTrailerWithYoutube(
                            id = id,
                            title = snippet?.title,
                            duration = contentDetails?.duration,
                            thumbnail = snippet?.thumbnails?.standard?.url)
                    }

                    trailer ?: MovieTrailerWithYoutube(null, null, null, null)
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