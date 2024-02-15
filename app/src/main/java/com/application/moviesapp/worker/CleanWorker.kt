package com.application.moviesapp.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.application.moviesapp.ui.utility.makeStatusNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File

@HiltWorker
class CleanerWorker  @AssistedInject constructor (@Assisted ctx: Context,
                                                  @Assisted params: WorkerParameters): CoroutineWorker(ctx, params) {
    companion object {
        private const val TAG = "CleanerWorker"
    }

    override suspend fun doWork(): Result {
        Timber.tag(TAG).d("Cleaning files")

        makeStatusNotification(
            message = "Cleaning files",
            context = applicationContext)

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val clean = listOf(
                    async {  removeVideoDirectory() },
                    async {  removeAudioDirectory() },
                    async {  createOutputDirectory() }
                )

                clean.awaitAll()
                Result.success()
            } catch (throwable: Throwable) {
                Timber.tag(TAG).e(throwable, "Cleaning failed")
                Result.failure()
            }
        }
    }

    private fun removeVideoDirectory() {
        val dir = File(applicationContext.filesDir, "video")
        if (dir.exists()) {
            dir.deleteRecursively()
            Timber.tag(TAG).d("Video dir deleted")
        }
    }

    private fun removeAudioDirectory() {
        val dir = File(applicationContext.filesDir, "audio")
        if (dir.exists()) {
            dir.deleteRecursively()
            Timber.tag(TAG).d("Audio dir deleted")
        }
    }

    private fun createOutputDirectory() {
        val dir = File(applicationContext.filesDir, "output")
        if (!dir.exists()) {
            dir.mkdir()

            val file = File(dir, "output.mp4")
            if (!file.exists()) {
                file.createNewFile()
            }
        } else {
            val file = File(dir, "output.mp4")
            if (!file.exists()) {
                file.createNewFile()
            }
        }
    }
}