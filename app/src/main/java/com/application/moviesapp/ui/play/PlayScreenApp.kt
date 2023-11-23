package com.application.moviesapp.ui.play

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.Player
import com.application.moviesapp.ui.viewmodel.PlayerUIState
import com.application.moviesapp.ui.viewmodel.PlayerViewModel
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PlayScreenApp(modifier: Modifier = Modifier,
                  playerViewModel: PlayerViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val systemUiController: SystemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false
    systemUiController.isNavigationBarVisible = false
    systemUiController.isSystemBarsVisible = false

    val playerUIState: PlayerUIState by playerViewModel.playerUIState.collectAsState()

    LaunchedEffect(key1 = null) {
        playerViewModel.playVideo(context, (context as Activity).intent.getStringExtra(PlayActivity.FILE_PATH) ?: return@LaunchedEffect)
    }

    Scaffold(
        containerColor = Color.Transparent,
    ) { paddingValues ->
        PlayScreen(
            modifier = modifier,
            player = playerViewModel.player,
            onPlayOrPause = playerViewModel::playOrPauseVideo,
            playerUIState = playerUIState,
            onScreenTouch = playerViewModel::onScreenTouch,
            onSeekTo = playerViewModel::onSeekTo)
    }
}