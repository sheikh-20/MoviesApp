package com.application.moviesapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject

data class MovieDownload(
    val title: String = "",
    val filePath: String = ""
)
data class PlayerUIState(
    val isPlaying: Boolean = true,
    val onScreenTouch: Boolean = true,
    val currentTime: Long = 0L,
    val totalDuration: Long = 0L,
    val bufferedPercentage: Int = 0,
    val movieDownload: MovieDownload = MovieDownload()
)
@HiltViewModel
class PlayerViewModel @Inject constructor(val player: Player): ViewModel() {

    private var _playerUIState = MutableStateFlow(PlayerUIState())
    val playerUIState: StateFlow<PlayerUIState> get() = _playerUIState.asStateFlow()

    
    fun playVideo(context: Context, videoTitle: String = "", filePath: String) {
        player.setMediaItem(MediaItem.fromUri(
            File(context.filesDir, "output/$filePath").toString()
        ))
        player.play()
        _playerUIState.update {
            it.copy(isPlaying = true, movieDownload = MovieDownload(title = videoTitle, filePath = filePath))
        }
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

    private fun onPlayerListener() = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)

            _playerUIState.update {
                it.copy(
                    totalDuration = player.duration.coerceAtLeast(0L),
                    currentTime = player.currentPosition.coerceAtLeast(0L),
                    bufferedPercentage = player.bufferedPercentage
                )
            }
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
        onPlayerListener()
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
        onPlayerListener()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    init {
        player.prepare()
        player.addListener(onPlayerListener())
    }
}