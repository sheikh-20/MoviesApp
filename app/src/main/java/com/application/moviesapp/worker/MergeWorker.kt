package com.application.moviesapp.worker

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.application.moviesapp.R
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.ui.utility.Mp4ParserAudioMuxer
import com.application.moviesapp.ui.utility.makeStatusNotification
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.UUID

@HiltWorker
class MergeWorker @AssistedInject constructor(@Assisted ctx: Context,
                                              @Assisted params: WorkerParameters,
                                              private val repository: MoviesRepository): CoroutineWorker(ctx, params){

    companion object {
        private const val TAG = "MergeWorker"
        const val DOWNLOAD_ENTITY = "DownloadEntity"
    }

    override suspend fun doWork(): Result {
        Timber.tag(TAG).d("Starting file merging")

        val downloadEntity = inputData.getString(DOWNLOAD_ENTITY)
        val gson = Gson().fromJson(downloadEntity, MovieDownloadEntity::class.java)

        makeStatusNotification(
            message = "Merging files",
            context = applicationContext)

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val mp4ParserAudioMuxer = Mp4ParserAudioMuxer(applicationContext)
                mp4ParserAudioMuxer.startMerging(gson.filePath ?: "")
//                saveMediaToStorage(
//                    filePath = File(applicationContext.filesDir, "/output/${gson.filePath}").path,
//                    isVideo = true,
//                    fileName = "${gson.filePath}"
//                )
                makeStatusNotification("Video saved successfully", applicationContext)

                repository.insertMovieDownload(gson)

                Result.success()
            }catch (throwable: Throwable) {
                makeStatusNotification("Merging failed", applicationContext)
                Timber.tag(TAG).e(throwable, "Merge failed")
                Result.failure()
            }
        }
    }
}

fun saveMediaToStorage(context: Context, filePath: String?, isVideo: Boolean, fileName: String) {
    filePath?.let {
        val values = ContentValues().apply {
            val folderName = if (isVideo) {
                Environment.DIRECTORY_MOVIES
            } else {
                Environment.DIRECTORY_PICTURES
            }
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "video/mp4")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                folderName + "/${context.getString(R.string.app_name)}/"
            )
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val collection = if (isVideo) {
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }
        val fileUri = context.contentResolver.insert(collection, values)

        fileUri?.let {
            if (isVideo) {
                context.contentResolver.openFileDescriptor(fileUri, "w").use { descriptor ->
                    descriptor?.let {
                        FileOutputStream(descriptor.fileDescriptor).use { out ->
                            val videoFile = File(filePath)
                            FileInputStream(videoFile).use { inputStream ->
                                val buf = ByteArray(8192)
                                while (true) {
                                    val sz = inputStream.read(buf)
                                    if (sz <= 0) break
                                    out.write(buf, 0, sz)
                                }
                            }
                        }
                    }
                }
            } else {
                context.contentResolver.openOutputStream(fileUri).use { out ->
                    val bmOptions = BitmapFactory.Options()
                    val bmp = BitmapFactory.decodeFile(filePath, bmOptions)
                    if (out != null) {
                        bmp.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    }
                    bmp.recycle()
                }
            }
            values.clear()
            values.put(
                if (isVideo) MediaStore.Video.Media.IS_PENDING else MediaStore.Images.Media.IS_PENDING,
                0
            )
            context.contentResolver.update(fileUri, values, null, null)
        }
    }
}