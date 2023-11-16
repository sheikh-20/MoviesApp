package com.application.moviesapp.ui.play

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import java.io.File

@OptIn(UnstableApi::class) @Composable
fun PlayScreen(modifier: Modifier = Modifier, player: Player? = null) {

    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        AndroidView(factory = { context ->
            PlayerView(context).also {
                it.player = player
            }
        },
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                )
    }
}

@Preview(showSystemUi = true, showBackground = true)
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