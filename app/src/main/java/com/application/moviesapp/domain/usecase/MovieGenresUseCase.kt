package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.mappers.toMovieGenre
import com.application.moviesapp.data.mappers.toYoutubeThumbnail
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieGenre
import com.application.moviesapp.domain.model.YoutubeThumbnail
import timber.log.Timber
import javax.inject.Inject

interface MovieGenresUseCase {
    suspend operator fun invoke(): Resource<MovieGenre>
}

class GetMovieGenreInteractor @Inject constructor(private val repository: MoviesRepository): MovieGenresUseCase {

    private companion object {
        const val TAG = "GetMovieGenreInteractor"
    }

    override suspend fun invoke(): Resource<MovieGenre> {
        return try {
            Resource.Loading

            val result = repository.getMovieGenres()

            if (result.isSuccessful) {
                val genres = result.body()?.toMovieGenre() ?: MovieGenre(null)
                Timber.tag(TAG).d(genres.toString())
                Resource.Success(data = genres)
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