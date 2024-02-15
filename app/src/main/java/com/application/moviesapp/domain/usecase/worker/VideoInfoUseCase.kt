package com.application.moviesapp.domain.usecase.worker

import androidx.work.WorkInfo
import com.application.moviesapp.data.python.WorkManagerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface VideoInfoUseCase {
    val readVideoInfo: Flow<WorkInfo>
    suspend fun getVideoInfo(videoId: String)
}

class VideoInfoInteractors @Inject constructor(private val repository: WorkManagerRepository): VideoInfoUseCase {
    override val readVideoInfo: Flow<WorkInfo>
        get() = repository.readVideoInfo

    override suspend fun getVideoInfo(videoId: String) {
        repository.getVideoInfo(videoId)
    }
}