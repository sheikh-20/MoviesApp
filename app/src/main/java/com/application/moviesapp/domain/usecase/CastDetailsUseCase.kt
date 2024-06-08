package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.mappers.toDomain
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.CastDetail
import com.application.moviesapp.domain.model.CastDetailWithImages
import com.application.moviesapp.domain.model.CastImages
import com.application.moviesapp.domain.model.CastMovieCredits
import com.application.moviesapp.domain.model.CastTvSeriesCredit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface CastDetailsUseCase {
    suspend operator fun invoke(personId: Int): Resource<CastDetailWithImages>
}

class GetCastDetailsInteractors @Inject constructor(private val repository: MoviesRepository): CastDetailsUseCase {

    private companion object {
        const val TAG = "CastDetailsInteractor"
    }

    override suspend fun invoke(personId: Int): Resource<CastDetailWithImages> =
        withContext(Dispatchers.IO) {
            return@withContext try {

                Resource.Loading

                val castDetailsReponse = async { repository.getCastDetails(personId) }
                val castImagesResponse = async { repository.getCastImages(personId) }
                val castMovieCreditsResponse = async { repository.getCastMovieCredits(personId) }
                val castTvSeriesCreditResponse = async { repository.getCastTvSeriesCredits(personId) }

                val castDetails = castDetailsReponse.await()
                val castImages = castImagesResponse.await()
                val castMovieCredits = castMovieCreditsResponse.await()
                val castTvSeriesCredit = castTvSeriesCreditResponse.await()

                if (castDetails.isSuccessful && castImages.isSuccessful && castMovieCredits.isSuccessful) {

                    val castDetailWithImages = CastDetailWithImages(
                        detail = castDetails.body()?.toDomain() ?: CastDetail(null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                        images = castImages.body()?.toDomain() ?: CastImages(null, null),
                        castMovieCredits = castMovieCredits.body()?.toDomain() ?: CastMovieCredits(null, null, null),
                        castTvSeriesCredits = castTvSeriesCredit.body()?.toDomain() ?: CastTvSeriesCredit(null, null, null)
                    )

                    Resource.Success(castDetailWithImages)
                } else if (castDetails.code() == 400 || castDetails.code() == 401) {
                    Timber.tag(TAG).e("404 erro - cast Details")
                    Resource.Failure(Throwable())
                } else if (castImages.code() == 400 || castImages.code() == 401) {
                    Timber.tag(TAG).e("404 error - cast Images")
                    Resource.Failure(Throwable())
                }
                else if (castMovieCredits.code() == 400 || castMovieCredits.code() == 401) {
                    Timber.tag(TAG).e("404 error - cast Images")
                    Resource.Failure(Throwable())
                }
                else if (castTvSeriesCredit.code() == 400 || castTvSeriesCredit.code() == 401) {
                    Timber.tag(TAG).e("404 error - cast Images")
                    Resource.Failure(Throwable())
                }
                else {
                    Timber.tag(TAG).e("Server error")
                    Resource.Failure(Throwable())
                }

                } catch (throwable: Throwable) {
                Resource.Failure(throwable)
            }
        }

}