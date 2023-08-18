package com.application.moviesapp.ui.home.download

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun DownloadScreen(modifier: Modifier = Modifier) {
    Column {
        Text(text = "Download Screen")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DownloadScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        DownloadScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DownloadScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        DownloadScreen()
    }
}