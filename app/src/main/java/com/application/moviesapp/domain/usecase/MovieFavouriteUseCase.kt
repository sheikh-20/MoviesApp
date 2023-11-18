package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieFavourite
import timber.log.Timber
import javax.inject.Inject

interface MovieFavouriteUseCase {
    suspend operator fun invoke(): Resource<List<MovieFavourite>>
}

class GetMovieFavouriteInteractor @Inject constructor(private val moviesRepository: MoviesRepository): MovieFavouriteUseCase {

    private companion object {
        const val TAG = "GetMovieFavouriteInteractor"
    }

    override suspend fun invoke(): Resource<List<MovieFavourite>> {
        return try {
            val response = moviesRepository.getMovieFavourite()

            if (response.isSuccessful) {
                val favourite = response.body()?.toMovies() ?: listOf()
                Timber.tag(TAG).d(favourite.toString())
                Resource.Success(favourite)
            } else {
                Timber.tag(TAG).e("${response.code()} -> ${response.errorBody()}")
                Resource.Failure(Throwable())
            }

        } catch (throwable: Throwable) {
            Timber.tag(TAG).e(throwable)
            Resource.Failure(throwable)
        }

    }
}