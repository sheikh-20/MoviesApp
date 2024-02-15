package com.application.moviesapp.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.application.moviesapp.data.python.DownloaderRepository
import com.application.moviesapp.ui.utility.makeStatusNotification
import com.chaquo.python.Python
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltWorker
class DownloadWorker @AssistedInject constructor(@Assisted ctx: Context,
                                                 @Assisted params: WorkerParameters,
                                                 private val repository: DownloaderRepository): CoroutineWorker(ctx, params) {

    companion object {
        private const val TAG = "DownloadWorker"
        const val VIDEO_URL = "videoUrl"
        const val VIDEO_ITAG = "video_itag"
        const val AUDIO_ITAG = "audio_itag"
    }

    override suspend fun doWork(): Result {
        val videoUrl = inputData.getString(VIDEO_URL) ?: ""
        val videoItag = inputData.getInt(VIDEO_ITAG, 0)
        val audioItag = inputData.getInt(AUDIO_ITAG, 0)

        delay(3_000L)

        makeStatusNotification(
            message = "Downloading files",
            context = applicationContext)

        return withContext(Dispatchers.IO) {

            return@withContext try {

                val task = listOf(
                    async {  repository.videoDownload(videoUrl, videoItag) },
                    async {  repository.audioDownload(videoUrl, audioItag) }
                )

                task.awaitAll()
                Result.success()
            } catch (throwable: Throwable) {
                Timber.tag(TAG).e(throwable, "Video download failed")
                Result.failure()
            }
        }
    }
}