package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.api.response.MovieUpdateFavouriteDto
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.MoviesRepository
import okhttp3.RequestBody
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

interface MovieUpdateFavouriteUseCase {
    suspend operator fun invoke(body: RequestBody): Resource<MovieUpdateFavouriteDto>
}

class MovieUpdateFavouriteInteractor @Inject constructor(private val moviesRepository: MoviesRepository): MovieUpdateFavouriteUseCase {

    private companion object {
        const val TAG = "MovieUpdateFavouriteInteractor"
    }

    override suspend operator fun invoke(body: RequestBody): Resource<MovieUpdateFavouriteDto> {
        return try {
            val response = moviesRepository.updateMovieFavourite(body)

            if (response.isSuccessful) {
                Resource.Success(response.body() ?: MovieUpdateFavouriteDto(null, null, null))
            } else {
                Timber.tag(TAG).e("${response.code()} ${response.errorBody()}")
                Resource.Failure(Throwable())
            }

        } catch (throwable: Throwable) {
            Timber.tag(TAG).e(throwable)
            Resource.Failure(throwable)
        }
    }
}