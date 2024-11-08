package com.application.moviesapp.ui.play

import android.app.Activity
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.domain.model.Stream
import com.application.moviesapp.ui.utility.toOneDecimal
import com.application.moviesapp.ui.utility.toYoutubeDuration
import com.application.moviesapp.ui.viewmodel.DetailsViewModel
import com.application.moviesapp.ui.viewmodel.DownloadUiState
import com.application.moviesapp.ui.viewmodel.DownloadViewModel
import com.application.moviesapp.ui.viewmodel.DownloadsUiState
import com.application.moviesapp.ui.viewmodel.PlayerStreamUIState
import com.application.moviesapp.ui.viewmodel.PlayerUIState
import com.application.moviesapp.ui.viewmodel.PlayerViewModel
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import timber.log.Timber
import java.io.File

private const val TAG = "PlayScreenApp"
@Composable
fun PlayScreenApp(modifier: Modifier = Modifier,
                  playerViewModel: PlayerViewModel = hiltViewModel(),
                  downloadViewModel: DownloadViewModel = hiltViewModel(),
                  detailsViewModel: DetailsViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val systemUiController: SystemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false
    systemUiController.isNavigationBarVisible = false
    systemUiController.isSystemBarsVisible = false

    val playerUIState: PlayerUIState by playerViewModel.playerUIState.collectAsStateWithLifecycle()
    val downloadUIState: DownloadsUiState by downloadViewModel.readAllDownload().collectAsState()

    val downloaderUiState by detailsViewModel.downloaderUiState.collectAsState()

    val playStreamUIState: PlayerStreamUIState by playerViewModel.playerStreamUIState.collectAsStateWithLifecycle()

    var drawerState by remember { mutableStateOf(DrawerValue.Closed) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }

    var sideSheetContent by remember { mutableStateOf<SideSheet>(SideSheet.Default) }

    LaunchedEffect(key1 = null) {
        when ((context as Activity).intent.getStringExtra(PlayActivity.FROM_SCREEN)) {
            Screen.Download.title -> {
                playerViewModel.playVideo(context, (context as Activity).intent.getStringExtra(PlayActivity.VIDEO_TITLE) ?: return@LaunchedEffect ,(context as Activity).intent.getStringExtra(PlayActivity.FILE_PATH) ?: return@LaunchedEffect)
            }
            else -> {
                playerViewModel.playVideoStream(videoId = (context as Activity).intent.getStringExtra(PlayActivity.VIDEO_ID) ?: return@LaunchedEffect, context = context)
                detailsViewModel.getVideoInfo(videoId = (context as Activity).intent.getStringExtra(PlayActivity.VIDEO_ID) ?: return@LaunchedEffect)
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
    ) { paddingValues ->

        BoxWithConstraints {

            val parentWidth = maxWidth
            val parentHeight = constraints.maxHeight.dp

            Box {
                when ((context as Activity).intent.getStringExtra(PlayActivity.FROM_SCREEN)) {
                    Screen.Download.title -> {
                        PlayScreen(
                            modifier = modifier,
                            player = playerViewModel.player,
                            onPlayOrPause = playerViewModel::playOrPauseVideo,
                            playerUIState = playerUIState,
                            onScreenTouch = playerViewModel::onScreenTouch,
                            onLockModeClick = playerViewModel::onLockMode,
                            onSeekTo = playerViewModel::onSeekTo,
                            onSeekForward = playerViewModel::onSeekForward,
                            onSeekBackward = playerViewModel::onSeekBackward,
                            onNextVideo = {
                                playerViewModel.onNextVideo(context, downloadUIState.data)
                            },
                            onPreviousVideo = {
                                playerViewModel.onPreviousVideo(context, downloadUIState.data)
                            },
                            videoTitle = playerUIState.movieDownload.title,
                            onDownloadClick = {
                                playerViewModel.saveMediaToStorage(
                                    context = context,
                                    filePath = File(context.filesDir, "/output/${playerUIState.movieDownload.filePath}").path,
                                    isVideo = true,
                                    fileName = playerUIState.movieDownload.title
                                )
                                Toast.makeText(context, "Video Downloaded", Toast.LENGTH_SHORT).show()
                            },
                            onVolumeClick = playerViewModel::onVolumeClick,
                            onPlaybackSpeedClick = {
                                drawerState = DrawerValue.Open
                                sideSheetContent = SideSheet.Playback
                                                   },
                            )
                    }

                    else -> {
//                        DetailPlayScreen(
//                            modifier = modifier,
//                            player = playerViewModel.player,
//                            onPlayOrPause = playerViewModel::playOrPauseVideo,
//                            playerStreamUIState = playStreamUIState,
//                            onScreenTouch = playerViewModel::onScreenTouch,
//                            onLockModeClick = playerViewModel::onLockMode,
//                            onVolumeClick = playerViewModel::onVolumeClick,
//                            onSeekTo = playerViewModel::onSeekTo,
//                            onSeekForward = playerViewModel::onSeekForward,
//                            onSeekBackward = playerViewModel::onSeekBackward,
//                            onPlaybackSpeedClick = {
//                                drawerState = DrawerValue.Open
//                                sideSheetContent = SideSheet.Playback
//                                                   },
//                            onDownloadClick = {
//                                drawerState = DrawerValue.Open
//                                sideSheetContent = SideSheet.Quality
//                            },
//                        )
                        val context = LocalContext.current

                        val youtubePlayer = remember {
                            YouTubePlayerView(context).apply {
                                (context as LifecycleOwner).lifecycle.addObserver(this)
                                enableAutomaticInitialization = false
                                initialize(object : AbstractYouTubePlayerListener() {
                                    override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                                        youTubePlayer.cueVideo( (context as Activity).intent.getStringExtra(PlayActivity.VIDEO_ID) ?: "", 0f)
                                    }
                                })
                            }
                        }

                        AndroidView(
                            {
                                youtubePlayer
                            }, modifier = Modifier.padding(paddingValues)
                                .fillMaxSize()
                                .aspectRatio(16 / 9f)
                        )
                    }
                }

                if (drawerState == DrawerValue.Open && playerUIState.onScreenTouch) {

                    when (sideSheetContent) {
                        SideSheet.Default -> {  }
                        SideSheet.Playback -> {
                            SideSheet(modifier = modifier
                                .width(width = parentWidth / 2)
                                .offset(x = parentWidth / 1.4f),
                                onDismiss = {
                                    drawerState = DrawerValue.Closed
                                    sideSheetContent = SideSheet.Default
                                            },
                                playbackSpeed = playbackSpeed,
                                onPlaybackSpeedChange = {
                                    playbackSpeed = (it)
                                    playerViewModel.onPlaybackChange(playbackSpeed)
                                },
                                maxWidth = parentWidth)
                        }
                        SideSheet.Quality -> {
                            DownloadSideSheet(
                                modifier = modifier
                                    .width(width = parentWidth / 2)
                                    .offset(x = parentWidth / 1.4f),
                                onDismiss = {
                                    drawerState = DrawerValue.Closed
                                    sideSheetContent = SideSheet.Default
                                },
                                onTrailerDownloadClick = detailsViewModel::videoDownload,
                                downloaderUiState = downloaderUiState
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SideSheet(modifier: Modifier = Modifier,
                        onDismiss: () -> Unit = {},
                      playbackSpeed: Float = 1.0f,
                      onPlaybackSpeedChange: (Float) -> Unit = { _ -> },
                      maxWidth: Dp = 0.dp

) {

        Column(modifier = modifier
            .padding(16.dp)
            .background(color = Color(0xFF28282B), shape = RoundedCornerShape(10))) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
                }

                Text(
                    text = "Playback speed",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Slider(
                modifier = Modifier.padding(start = 16.dp, end = maxWidth / 4.5f),
                value = playbackSpeed,
                onValueChange = onPlaybackSpeedChange,
                valueRange = 0.1f .. 2f
                )

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(start = 16.dp, end = maxWidth / 4.5f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {

                Text(text = "0.1x", color = Color.White, fontWeight = FontWeight.Bold)

                when (playbackSpeed.toDouble().toOneDecimal) {
                    "1.0" -> {
                        Text(text = "Normal", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    else -> {
                        Text(text = playbackSpeed.toDouble().toOneDecimal, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }

                Text(text = "2x", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DownloadSideSheet(modifier: Modifier = Modifier,
                              onDismiss: () -> Unit = {},
                              onTrailerDownloadClick: (String, Stream, Stream, MovieDownloadEntity) -> Unit = { _, _, _, _ -> },
                              downloaderUiState: DownloadUiState = DownloadUiState.Default,
) {

    val context = LocalContext.current
    var selectedVideoQuality by remember { mutableIntStateOf(0) }

    Column(modifier = modifier
        .padding(16.dp)
        .background(color = Color(0xFF28282B), shape = RoundedCornerShape(10))) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDismiss) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
            }

            Text(
                text = "Download Quality",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }

//        Slider(
//            modifier = Modifier.padding(start = 16.dp, end = maxWidth / 4.5f),
//            value = playbackSpeed,
//            onValueChange = onPlaybackSpeedChange,
//            valueRange = 0.1f .. 2f
//        )
//
//        Row(modifier = Modifier
//            .fillMaxWidth()
//            .padding(bottom = 16.dp)
//            .padding(start = 16.dp, end = maxWidth / 4.5f),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween) {
//
//            Text(text = "0.1x", color = Color.White, fontWeight = FontWeight.Bold)
//
//            when (playbackSpeed.toDouble().toOneDecimal) {
//                "1.0" -> {
//                    Text(text = "Normal", color = Color.White, fontWeight = FontWeight.Bold)
//                }
//                else -> {
//                    Text(text = playbackSpeed.toDouble().toOneDecimal, color = Color.White, fontWeight = FontWeight.SemiBold)
//                }
//            }
//
//            Text(text = "2x", color = Color.White, fontWeight = FontWeight.Bold)
//        }

        when (downloaderUiState) {
            is DownloadUiState.Default -> {
                Timber.tag(TAG).d("Video Downloader")
                CircularProgressIndicator(modifier = modifier
                    .fillMaxWidth()
                    .size(28.dp), strokeWidth = 4.dp, strokeCap = StrokeCap.Round)
            }
            is DownloadUiState.Loading -> {
                Timber.tag(TAG).d("Downloader Loading")
                CircularProgressIndicator(modifier = modifier
                    .fillMaxWidth()
                    .size(28.dp), strokeWidth = 4.dp, strokeCap = StrokeCap.Round)
            }
            is DownloadUiState.Complete -> {
                Timber.tag(TAG).d(downloaderUiState.toString())

//                AssistChip(
//                    onClick = {
////                        onTrailerDownloadClick(movieTrailerWithYoutube.id ?: return@AssistChip,
////                            downloaderUiState.videoStreams.first(),
////                            downloaderUiState.audioStreams ?: return@AssistChip,
////                            MovieDownloadEntity(
////                                backdropPath = movieTrailerWithYoutube.thumbnail,
////                                runtime = movieTrailerWithYoutube.duration?.toYoutubeDuration ?: "",
////                                title = movieTrailerWithYoutube.title,
////                                filePath = movieTrailerWithYoutube.title?.replace(":", "_") + ".mp4"
////                            )
////                        )
//                    },
//                    label = {
//                        Text(text = downloaderUiState.videoStreams.first().resolution.removeSurrounding("\""), color = MaterialTheme.colorScheme.primary)
//                    },
//                    shape = RoundedCornerShape(50)
//                )

                val color = MaterialTheme.colorScheme.primary

                LazyColumn {
                    items(downloaderUiState.videoStreams.size) {
                        Row(modifier = Modifier.clickable(
                            onClick = {
                        onTrailerDownloadClick((context as Activity).intent.getStringExtra(PlayActivity.VIDEO_ID) ?: return@clickable,
                            downloaderUiState.videoStreams.first(),
                            downloaderUiState.audioStreams ?: return@clickable,
                            MovieDownloadEntity(
                                backdropPath = downloaderUiState.videoThumbnail,
                                runtime = null,
                                title = downloaderUiState.videoTitle,
                                filePath = downloaderUiState.videoTitle?.replace(":", "_") + ".mp4"
                            )
                        )

                                Toast.makeText(context, "Video downloading..", Toast.LENGTH_SHORT).show()
                                onDismiss()

                                      },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                            IconButton(onClick = { /*TODO*/ }) {
                                if (selectedVideoQuality == it) {
                                    Icon(imageVector = Icons.Rounded.Check,
                                        contentDescription = null,
                                        tint = color)
                                }
                            }

                            Text(text = downloaderUiState.videoStreams[it].resolution.removeSurrounding("\""),
                                color = if (selectedVideoQuality == it) color else Color.White,
                                fontWeight = FontWeight.SemiBold,
                                )

                        }
                    }
                }
            }
        }
    }
}

enum class SideSheet {
    Default, Playback, Quality
}