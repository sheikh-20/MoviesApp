package com.application.moviesapp.data.python

import androidx.lifecycle.asFlow
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.domain.model.Stream
import com.application.moviesapp.worker.CleanerWorker
import com.application.moviesapp.worker.VideoInfoWorker
import com.application.moviesapp.worker.DownloadWorker
import com.application.moviesapp.worker.MergeWorker
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

interface WorkManagerRepository {
    val readVideoInfo: Flow<WorkInfo>
    suspend fun getVideoInfo(videoUrl: String)

    suspend fun videoDownload(videoUrl: String, videoStream: Stream, audioStream: Stream?, movieDownloadEntity: MovieDownloadEntity?)
}

class WorkManagerRepositoryImpl @Inject constructor(private val workManager: WorkManager): WorkManagerRepository {
    override val readVideoInfo: Flow<WorkInfo>
        get() =  workManager.getWorkInfosByTagLiveData(VideoInfoWorker.TAG_OUTPUT).asFlow().mapNotNull {
            if (it.isNotEmpty()) it.first() else null
        }

    override suspend fun getVideoInfo(videoUrl: String) {
        val videoInfoBuilder = OneTimeWorkRequestBuilder<VideoInfoWorker>()
        videoInfoBuilder.setInputData(
            Data.Builder()
                .putString(VideoInfoWorker.VIDEO_URL, videoUrl)

                .build()
        )
        videoInfoBuilder.addTag(VideoInfoWorker.TAG_OUTPUT)

        workManager.enqueueUniqueWork(VideoInfoWorker.WORK_NAME, ExistingWorkPolicy.REPLACE,videoInfoBuilder.build())
    }

    override suspend fun videoDownload(videoUrl: String, videoStream: Stream, audioStream: Stream?, movieDownloadEntity: MovieDownloadEntity?)  {
        val formattedResultVideo = videoStream.iTag.substring(1, videoStream.iTag.length.minus(1))
        val formattedResultAudio = audioStream?.iTag?.substring(1, audioStream.iTag.length.minus(1)) ?: "0"

        val downloadBuilder = OneTimeWorkRequestBuilder<DownloadWorker>()

        downloadBuilder.setInputData(
            Data.Builder().putString(DownloadWorker.VIDEO_URL, videoUrl)
                .putInt(DownloadWorker.VIDEO_ITAG, formattedResultVideo.toInt())
                .putInt(DownloadWorker.AUDIO_ITAG, formattedResultAudio.toInt())
                .build())

        val mergeBuilder = OneTimeWorkRequestBuilder<MergeWorker>()
        val gson = Gson().toJson(movieDownloadEntity)

        mergeBuilder.setInputData(
            Data.Builder().putString(MergeWorker.DOWNLOAD_ENTITY, gson)
                .build()
        )

        workManager.beginUniqueWork("download", ExistingWorkPolicy.REPLACE, OneTimeWorkRequestBuilder<CleanerWorker>().build())
            .then(downloadBuilder.build())
            .then(mergeBuilder.build())
            .enqueue()
    }

}
