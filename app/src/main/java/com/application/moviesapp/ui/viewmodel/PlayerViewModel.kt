package com.application.moviesapp.ui.viewmodel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import com.application.moviesapp.R
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.ui.utility.formatMinSec
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

data class MovieDownload(
    val title: String = "",
    val filePath: String = ""
)
data class PlayerUIState(
    val isPlaying: Boolean = true,
    val onScreenTouch: Boolean = true,
    val isLockMode: Boolean = false,
    val currentTime: Long = 0L,
    val totalDuration: Long = 0L,
    val bufferedPercentage: Int = 0,
    val movieDownload: MovieDownload = MovieDownload(),
    val hasVolume: Boolean = true
)
@HiltViewModel
class PlayerViewModel @Inject constructor(val player: Player): ViewModel() {

    private companion object {
        const val TAG = "PlayerViewModel"
    }

    private var _playerUIState = MutableStateFlow(PlayerUIState())
    val playerUIState: StateFlow<PlayerUIState> get() = _playerUIState.asStateFlow()


    private val handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    fun playVideo(context: Context, videoTitle: String = "", filePath: String) {
        player.setMediaItem(MediaItem.fromUri(
            File(context.filesDir, "output/$filePath").toString()
        ))
        player.play()
        _playerUIState.update {
            it.copy(isPlaying = true, movieDownload = MovieDownload(title = videoTitle, filePath = filePath))
        }

        runnable = object : Runnable {
            override fun run() {

                _playerUIState.update {
                    it.copy(
                        totalDuration = player.duration.coerceAtLeast(0L),
                        currentTime = player.currentPosition.coerceAtLeast(0L),
                        bufferedPercentage = player.bufferedPercentage
                    )
                }
                handler.postDelayed(this,500L)
            }
        }
        runnable.run()
    }

    fun playOrPauseVideo() {
        if (player.isPlaying) {
            player.pause()
            _playerUIState.update {
                it.copy(isPlaying = false)
            }
        } else {
            player.play()
            _playerUIState.update {
                it.copy(isPlaying = true)
            }
        }
    }

    fun onScreenTouch() {
        _playerUIState.update {
            it.copy(onScreenTouch = it.onScreenTouch.not())
        }
    }

    fun onLockMode() {
        _playerUIState.update {
            it.copy(isLockMode = it.isLockMode.not())
        }
    }

    fun onSeekTo(seekValue: Float) {
        player.seekTo(seekValue.toLong())
    }

    fun onSeekForward() {
        player.seekTo(player.currentPosition + 10_000L)
    }

    fun onSeekBackward() {
        player.seekTo(player.currentPosition - 10_000L)
    }

    fun onNextVideo(context: Context, videos: List<MovieDownloadEntity>) {
        var nextIndex: Int = 0

        //next video
        videos.mapIndexed { index, video ->
            if (video.filePath == _playerUIState.value.movieDownload.filePath) {
                if (videos.last().filePath == video.filePath) {
                    nextIndex = index
                } else {
                    nextIndex = index.inc()
                    ""
                }
            } else ""
        }

        playVideo(context = context, videoTitle = videos[nextIndex].title ?: "", filePath = videos[nextIndex].filePath ?: "")
//        onPlayerListener()
    }

    fun onPreviousVideo(context: Context, videos: List<MovieDownloadEntity>) {
        var nextIndex: Int = 0

        //next video
        videos.mapIndexed { index, video ->
            if (video.filePath == _playerUIState.value.movieDownload.filePath) {
                if (videos.first().filePath == video.filePath) {
                    nextIndex = index
                } else {
                    nextIndex = index.dec()
                    ""
                }
            } else ""
        }

        playVideo(context = context, videoTitle = videos[nextIndex].title ?: "", filePath = videos[nextIndex].filePath ?: "")
//        onPlayerListener()
    }

    fun onVolumeClick() {
        _playerUIState.update {
            it.copy(hasVolume = it.hasVolume.not())
        }
        if (playerUIState.value.hasVolume) {
            player.volume = 1.0f
        } else player.volume = 0f
    }

    fun onPlaybackChange(speed: Float) {
        player.setPlaybackSpeed(speed)
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
                        bmp.compress(Bitmap.CompressFormat.JPEG, 90, out)
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

    override fun onCleared() {
        super.onCleared()
        player.release()
        handler.removeCallbacks(runnable)
    }

    init {
        player.prepare()
    }
}