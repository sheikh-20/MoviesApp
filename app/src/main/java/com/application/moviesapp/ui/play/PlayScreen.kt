package com.application.moviesapp.ui.play

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material.icons.rounded.Forward10
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.FullscreenExit
import androidx.compose.material.icons.rounded.KeyboardVoice
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay10
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.Subtitles
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.formatMinSec
import com.application.moviesapp.ui.viewmodel.PlayerUIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.io.File

private const val TAG = "PlayScreen"
@Composable
fun PlayScreen(modifier: Modifier = Modifier,
               player: Player? = null,
               onPlayOrPause: () -> Unit = { },
               playerUIState: PlayerUIState = PlayerUIState(),
               onScreenTouch: () -> Unit = {},
               onLockModeClick: () -> Unit = { },
               onSeekTo: (Float) -> Unit = { },
               onSeekForward: () -> Unit = {  },
               onSeekBackward: () -> Unit = {  },
               onNextVideo: () -> Unit = {  },
               onPreviousVideo: () -> Unit = { },
               videoTitle: String = "",
               onDownloadClick: () -> Unit = {  },
               onVolumeClick: (String) -> Unit = {  _ -> },
               onPlaybackSpeedClick: () -> Unit = {  },
) {

    val context = LocalContext.current

    var fullScreenMode by remember {
        mutableStateOf(false)
    }

    var scaleFactor by remember { mutableFloatStateOf(0f) }
    Timber.tag(TAG).d("Pinch to zoom called! $scaleFactor")

    val modifierOriginalScreen = modifier
        .fillMaxSize()
        .clickable(
            onClick = onScreenTouch,
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        )

    val modifierFullScreen = modifier
        .fillMaxSize()
        .aspectRatio(16 / 9f)
        .clickable(
            onClick = onScreenTouch,
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        )

    Box(modifier = modifier.fillMaxSize()
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                scaleFactor = if (zoom >= 1f) 1f else 0f
                fullScreenMode = if (scaleFactor >= 1f) true else false
            }
        }) {

        AndroidView(
            factory = { context ->

                PlayerView(context).also {
                    it.player = player
                    it.layoutParams =
                        FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    it.useController = false
                }
            },
            update = {},
            modifier = if (fullScreenMode) modifierFullScreen else modifierOriginalScreen
        )

        if (playerUIState.onScreenTouch) {
                CustomPlayerUI(
                    videoTitle = videoTitle,
                    player = player,
                    onPlayOrPause = onPlayOrPause,
                    playerUIState = playerUIState,
                    onSeekTo = onSeekTo,
                    onFullScreenModeClicked = { fullScreenMode = it },
                    isFullScreen = fullScreenMode,
                    onSeekForward = onSeekForward,
                    onSeekBackward = onSeekBackward,
                    onNextVideo = onNextVideo,
                    onPreviousVideo = onPreviousVideo,
                    onLockModeClick = onLockModeClick,
                    onDownloadClick = onDownloadClick,
                    onVolumeClick = onVolumeClick,
                    onPlaybackSpeedClick = onPlaybackSpeedClick,
                )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CustomPlayerUI(modifier: Modifier = Modifier,
                           videoTitle: String = "",
                           player: Player? = null,
                           onPlayOrPause: () -> Unit = {},
                           playerUIState: PlayerUIState = PlayerUIState(),
                           onSeekTo: (Float) -> Unit = { },
                           onFullScreenModeClicked: (Boolean) -> Unit = { },
                           isFullScreen: Boolean = false,
                           onSeekForward: () -> Unit = {  },
                           onSeekBackward: () -> Unit = {  },
                           onNextVideo: () -> Unit = {  },
                           onPreviousVideo: () -> Unit = {  },
                           onLockModeClick: () -> Unit = {  },
                           onDownloadClick: () -> Unit = {  },
                           onVolumeClick: (String) -> Unit = { _ -> },
                           onPlaybackSpeedClick: () -> Unit = {  }) {

    val context = LocalContext.current

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {

        if (playerUIState.isLockMode.not()) {
            Row(modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                IconButton(onClick = { (context as Activity).finish() }) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                }

                Text(
                    text = videoTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                IconButton(onClick = { onPlaybackSpeedClick() }) {
                    Icon(imageVector = Icons.Rounded.Speed, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.Subtitles, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.KeyboardVoice, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.Cast, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }

        Spacer(modifier = modifier.weight(1f))

        Column(modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {

            if (playerUIState.isLockMode.not()) {
                Row(modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                    Text(text = playerUIState.currentTime.formatMinSec(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)

                    Slider(value = playerUIState.currentTime.toFloat(),
                        onValueChange = { onSeekTo(it) },
                        modifier = modifier.weight(1f),
                        valueRange = 0f..playerUIState.totalDuration.toFloat(),
                        colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.onSecondary))

                    Text(text = playerUIState.totalDuration.formatMinSec(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Row(modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    IconButton(onClick = onLockModeClick) {
                        Icon(imageVector = if (playerUIState.isLockMode.not()) Icons.Rounded.LockOpen else Icons.Rounded.Lock,
                            contentDescription = null,
                            modifier = modifier.size(24.dp), tint = MaterialTheme.colorScheme.onPrimary)
                    }

                    if (playerUIState.isLockMode.not()) {
                        IconButton(onClick = { onVolumeClick((context as Activity).intent.getStringExtra(PlayActivity.FROM_SCREEN) ?: return@IconButton) }) {
                            Icon(imageVector = if (playerUIState.hasVolume) Icons.Rounded.VolumeUp else Icons.Rounded.VolumeOff,
                                contentDescription = null,
                                modifier = modifier.size(24.dp), tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }

                Spacer(modifier = modifier.weight(1f))

                if (playerUIState.isLockMode.not()) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {


                        IconButton(onClick = onSeekBackward) {
                            Icon(imageVector = Icons.Rounded.Replay10,
                                contentDescription = null,
                                modifier = modifier.size(30.dp), tint = MaterialTheme.colorScheme.onPrimary)
                        }


                        IconButton(onClick = onPreviousVideo) {
                            Icon(imageVector = Icons.Rounded.SkipPrevious,
                                contentDescription = null,
                                modifier = modifier.size(36.dp), tint = MaterialTheme.colorScheme.onPrimary)
                        }


                        IconButton(onClick = onPlayOrPause) {
                            if (playerUIState.isPlaying) {
                                Icon(imageVector = Icons.Rounded.Pause,
                                    contentDescription = null,
                                    modifier = modifier.size(80.dp), tint = MaterialTheme.colorScheme.onPrimary)
                            } else {
                                Icon(imageVector = Icons.Rounded.PlayArrow,
                                    contentDescription = null,
                                    modifier = modifier.size(80.dp), tint = MaterialTheme.colorScheme.onPrimary)
                            }
                        }


                        IconButton(onClick = onNextVideo) {
                            Icon(imageVector = Icons.Rounded.SkipNext,
                                contentDescription = null,
                                modifier = modifier.size(36.dp), tint = MaterialTheme.colorScheme.onPrimary)
                        }


                        IconButton(onClick = onSeekForward) {
                            Icon(imageVector = Icons.Rounded.Forward10,
                                contentDescription = null,
                                modifier = modifier.size(30.dp), tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }

                Spacer(modifier = modifier.weight(1f))

                if (playerUIState.isLockMode.not()) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {


                        IconButton(onClick = { onFullScreenModeClicked(isFullScreen.not()) }) {
                            Icon(imageVector = if (isFullScreen) Icons.Rounded.FullscreenExit else Icons.Rounded.Fullscreen,
                                contentDescription = null,
                                modifier = modifier.size(24.dp), tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, )
@Composable
private fun PlayScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        PlayScreen()
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PlayScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        PlayScreen()
    }
}