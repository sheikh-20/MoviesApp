package com.application.moviesapp.domain.usecase.worker

import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.data.python.WorkManagerRepository
import com.application.moviesapp.domain.model.Stream
import javax.inject.Inject

interface DownloadUseCase {
    suspend operator fun invoke(videoId: String, videoStream: Stream, audioStream: Stream?, movieDownloadEntity: MovieDownloadEntity?)
}

class GetDownloadInteractor @Inject constructor(private val repository: WorkManagerRepository): DownloadUseCase {
    override suspend fun invoke(videoId: String, videoStream: Stream, audioStream: Stream?, movieDownloadEntity: MovieDownloadEntity?) {
        repository.videoDownload(videoId, videoStream, audioStream, movieDownloadEntity)
    }
}