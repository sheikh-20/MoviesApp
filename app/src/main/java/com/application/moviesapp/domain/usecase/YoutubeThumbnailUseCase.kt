package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.mappers.toYoutubeThumbnail
import com.application.moviesapp.data.repository.YoutubeRepository
import com.application.moviesapp.domain.model.YoutubeThumbnail
import timber.log.Timber
import javax.inject.Inject

interface YoutubeThumbnailUseCase {
    suspend operator fun invoke(vararg part: String, id: String): Resource<YoutubeThumbnail>
}

class YoutubeThumbnailInteractor @Inject constructor(private val repository: YoutubeRepository): YoutubeThumbnailUseCase {

    private companion object {
        const val TAG = "YoutubeDetailsInteractor"
    }

    override suspend fun invoke(vararg part: String, id: String): Resource<YoutubeThumbnail> {
        return try {
            val result = repository.videoThumbnail(part = part, id = id)

            if (result.isSuccessful) {
                val thumbnail = result.body()?.toYoutubeThumbnail() ?: YoutubeThumbnail(null)
                Timber.tag(TAG).d(thumbnail.toString())
                Resource.Success(data = thumbnail)
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

